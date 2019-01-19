using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using AutoMapper;
using System.IdentityModel.Tokens.Jwt;
using ASP.Helpers;
using Microsoft.Extensions.Options;
using System.Text;
using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using ASP.Services;
using ASP.Dtos;
using ASPCoreApi.Models;
using System.Threading.Tasks;
using ASPCoreApi.Services;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using ASPCoreApi.Helpers;

namespace ASP.Dtos
{
    [Authorize]
    [Route("[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private IUserService _userService;
        private IStatsService _statsService;
        private IVisitedSightsService _visitedSightsService;
        private IGameStatsService _gameStatsService;
        private IMapper _mapper;
        private readonly AppSettings _appSettings;
        private readonly DatabaseContext _context;

        public UsersController(

            IUserService userService,
            IVisitedSightsService visitedSightsService,
            IStatsService statsService,
            IGameStatsService gameStatsService,
            IMapper mapper,
            IOptions<AppSettings> appSettings, DatabaseContext context)
        {
            
            _userService = userService;
            _mapper = mapper;
            _appSettings = appSettings.Value;
            _context = context;
            _visitedSightsService = visitedSightsService;
            _statsService = statsService;
            _gameStatsService = gameStatsService;
        }

        [AllowAnonymous]
        [HttpPost("authenticate")]
        public IActionResult Authenticate([FromBody]UserDto userDto)
        {
            var user = _userService.Authenticate(userDto.Username, userDto.Password);
            
            if (user == null)
                return BadRequest(new { message = "Username or password is incorrect" });
                       
            //.........................................................
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.ASCII.GetBytes(_appSettings.Secret);
            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(new Claim[]
                {
                    new Claim(ClaimTypes.Name, user.Id.ToString())
                }),
                Expires = DateTime.UtcNow.AddDays(7),
                SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature)
            };
            var token = tokenHandler.CreateToken(tokenDescriptor);
            var tokenString = tokenHandler.WriteToken(token);           
            user.Stats = _statsService.getByUserId(user.Id);
            user.gameStats =  _gameStatsService.getByUserId(user.Id);
            user.visitedSights = _visitedSightsService.getByUserId(user.Id);
            user.PasswordHash = null;
            user.PasswordSalt = null;

            string _json = JsonConvert.SerializeObject(user, Formatting.Indented, new JsonSerializerSettings
            {
                ReferenceLoopHandling = ReferenceLoopHandling.Ignore
            });
            JObject jsonUser = JObject.Parse(_json);

            var authObj = new AuthenticationObject();
            authObj.user = jsonUser;
            authObj.token = tokenString;
            return Ok(authObj);
        }

        [AllowAnonymous]
        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody]UserDto userDto)
        {
           
            var user = _mapper.Map<Users>(userDto);
            var stats = new Statistics
            {
                totalFailed = 0,
                totalLost = 0,
                totalScore = 0,
                totalSucces = 0,
                highestScore = 0
            };
            user.Stats = stats;
            var gameStats = new GameStats
            {
                lifePoints = 2,
                rifle = 0,
                freezeGun = 0,
                pushBackGun = 0,
                coins = 0,

            };
            user.skinId = 1;
            user.gameStats = gameStats;

            var _visitedSights = new List<VisitedSights>();
            for (int i = 0; i < 23; i++)
            {
                var visitedSight = new VisitedSights
                {
                    buildingId = i + 1,
                    isChecked = false,
                };
                _visitedSights.Add(visitedSight);
            }
            user.visitedSights = _visitedSights;
            
            try
            {
                             
                _userService.Create(user, userDto.Password);                
                await _context.SaveChangesAsync();
                return Ok();
            }
            catch (AppException ex)
            {               
                return BadRequest(new { message = ex.Message });
            }
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            var users = _userService.GetAll();
            IList<JObject> jsonUserList = new List<JObject>();

            foreach (var user in users)
            {
                user.Stats = _statsService.getByUserId(user.Id);
                user.gameStats = _gameStatsService.getByUserId(user.Id);
                user.visitedSights = _visitedSightsService.getByUserId(user.Id);
                user.PasswordHash = null;
                user.PasswordSalt = null;
            }
            
            foreach (var user in users)
            {
                string _json = JsonConvert.SerializeObject(user, Formatting.Indented, new JsonSerializerSettings
                {
                    ReferenceLoopHandling = ReferenceLoopHandling.Ignore
                });
                JObject jsonUser = JObject.Parse(_json);
                jsonUserList.Add(jsonUser);
            }                     
            return Ok(jsonUserList);
        }

        [HttpGet("{id}")]
        public IActionResult GetById(int id)
        {
            var user = _userService.GetById(id);

            user.Stats = _statsService.getByUserId(user.Id);
            user.gameStats = _gameStatsService.getByUserId(user.Id);
            user.visitedSights = _visitedSightsService.getByUserId(user.Id);
            user.PasswordHash = null;
            user.PasswordSalt = null;

            
            string _json = JsonConvert.SerializeObject(user, Formatting.Indented, new JsonSerializerSettings
            {
                ReferenceLoopHandling = ReferenceLoopHandling.Ignore
            });
            JObject jsonUser = JObject.Parse(_json);

            return Ok(jsonUser);
        }
        [HttpGet("gettop10/{orderBy}")]
        public IActionResult GetTop10([FromRoute]string orderBy)
        {
            var top10List = _userService.getTop10(orderBy);
                       
            return Ok(top10List);
        }
        [HttpGet("gethighscoreuser/{id}/{orderBy}")]
        public IActionResult getUserhsProfile([FromRoute]string orderBy,[FromRoute] int id)
        {
            var highscoreProfile = _userService.getUserHsList(orderBy,id);
                      
            return Ok(highscoreProfile);
        }

        [AllowAnonymous]
        [HttpPut("updateuser/{id}")]
        public IActionResult Update(int id,[FromBody]UserDto userDto)
        {
            // map dto to entity and set id
            var user = _mapper.Map<Users>(userDto);
            user.Id = id;

            try
            {
                // save 
                _userService.Update(user, userDto.Password);
                return Ok();
            }
            catch (AppException ex)
            {
                // return error message if there was an exception
                return BadRequest(new { message = ex.Message });
            }
        }
        [AllowAnonymous]
        [HttpPut("updatestats/{id}")]
        public async Task<IActionResult> UpdateStats(int id, [FromBody]UserDto userDto)
        {
            // map dto to entity and set id
            var user = _mapper.Map<Users>(userDto);
            user.Id = id;

            try
            {
                // save 
                _statsService.UpdateStats(user);
                await _context.SaveChangesAsync();
                return Ok();
            }
            catch (AppException ex)
            {
                // return error message if there was an exception
                return BadRequest(new { message = ex.Message });
            }
        }

        [HttpPut("updategamestats/{id}")]
        public IActionResult UpdateGameStats(int id, [FromBody]UserDto userDto)
        {
            // map dto to entity and set id
            var user = _mapper.Map<Users>(userDto);
            user.Id = id;

            try
            {
                // save 
                _gameStatsService.UpdateGameStats(user);
                return Ok();
            }
            catch (AppException ex)
            {
                // return error message if there was an exception
                return BadRequest(new { message = ex.Message });
            }
        }

        [HttpPut("updatevisitedsights/{id}")]
        public IActionResult UpdateVisitedSights(int id, [FromBody]UserDto userDto)
        {
            // map dto to entity and set id
            var user = _mapper.Map<Users>(userDto);
            user.Id = id;

            try
            {
                // save 
                _visitedSightsService.UpdateVisitedSights(user);
                return Ok();
            }
            catch (AppException ex)
            {
                // return error message if there was an exception
                return BadRequest(new { message = ex.Message });
            }
        }

        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            _userService.Delete(id);
            return Ok();
        }
    }
}