using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using ASPCoreApi.Models;

namespace ASPCoreApi.Controllers
{
    [Route("[controller]")]
    [ApiController]
    public class GameStatsController : ControllerBase
    {
        private readonly DatabaseContext _context;

        public GameStatsController(DatabaseContext context)
        {
            _context = context;
        }

        // GET: api/GameStats
        [HttpGet]
        public IEnumerable<GameStats> GetGameStats()
        {
            return _context.gameStats;
        }

        // GET: api/GameStats/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetGameStats([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var gameStats = await _context.gameStats.FindAsync(id);

            if (gameStats == null)
            {
                return NotFound();
            }

            return Ok(gameStats);
        }

        // PUT: api/GameStats/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutGameStats([FromRoute] int id, [FromBody] GameStats gameStats)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != gameStats.Id)
            {
                return BadRequest();
            }

            _context.Entry(gameStats).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!GameStatsExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/GameStats
        [HttpPost]
        public async Task<IActionResult> PostGameStats([FromBody] GameStats gameStats)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.gameStats.Add(gameStats);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetGameStats", new { id = gameStats.Id }, gameStats);
        }

        // DELETE: api/GameStats/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteGameStats([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var gameStats = await _context.gameStats.FindAsync(id);
            if (gameStats == null)
            {
                return NotFound();
            }

            _context.gameStats.Remove(gameStats);
            await _context.SaveChangesAsync();

            return Ok(gameStats);
        }

        private bool GameStatsExists(int id)
        {
            return _context.gameStats.Any(e => e.Id == id);
        }
    }
}