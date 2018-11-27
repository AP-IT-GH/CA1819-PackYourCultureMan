﻿using System;
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

namespace ASP.Dtos
{
    [Authorize]
    [Route("[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private IUserService _userService;
        
        private IMapper _mapper;
        private readonly AppSettings _appSettings;
        private readonly DatabaseContext _context;

        public UsersController(
            
            IUserService userService,
            IMapper mapper,
            IOptions<AppSettings> appSettings, DatabaseContext context)
        {
            
            _userService = userService;
            _mapper = mapper;
            _appSettings = appSettings.Value;
            _context = context;
        }

        [AllowAnonymous]
        [HttpPost("authenticate")]
        public IActionResult Authenticate([FromBody]UserDto userDto)
        {
            var user = _userService.Authenticate(userDto.Username, userDto.Password);
            
            if (user == null)
                return BadRequest(new { message = "Username or password is incorrect" });
            var stats = _context.stats.Find(user.StatsId);
            var gameStats = _context.gameStats.Find(user.gameStatsId);
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
            // return basic user info (without password) and token to store client side
            
            
            return Ok(new
            {
                Id = user.Id,
                Username = user.Username,
                FirstName = user.FirstName,
                LastName = user.LastName,
                Token = tokenString,
                Email = user.Email,
                stats = stats,   
                StatsId = user.StatsId,
                gameStats = user.gameStats
       
            });
        }

        [AllowAnonymous]
        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody]UserDto userDto)
        {
            // map dto to entity
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
                pushBackGun = 0

            };
            user.gameStats = gameStats;
            try
            {
                // save 
              

                _userService.Create(user, userDto.Password);
                
                await _context.SaveChangesAsync();
                return Ok();
            }
            catch (AppException ex)
            {
                // return error message if there was an exception
                return BadRequest(new { message = ex.Message });
            }
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            var users = _userService.GetAll();
            var userDtos = _mapper.Map<IList<UserDto>>(users);
            return Ok(userDtos);
        }

        [HttpGet("{id}")]
        public IActionResult GetById(int id)
        {
            var user = _userService.GetById(id);
            var userDto = _mapper.Map<UserDto>(user);
            return Ok(userDto);
        }

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
        [HttpPut("updatestats/{id}")]
        public IActionResult UpdateStats(int id, [FromBody]UserDto userDto)
        {
            // map dto to entity and set id
            var user = _mapper.Map<Users>(userDto);
            user.Id = id;

            try
            {
                // save 
                _userService.UpdateStats(user);
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