using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ASPCoreApi.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ASPCoreApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class StatController : ControllerBase
    {
        private readonly DatabaseContext _context;

        public StatController(DatabaseContext context)
        {
            _context = context;
        }
        [HttpGet("{id}")]
        public async Task<IActionResult> GetStats([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var sight = await _context.statistics.FindAsync(id);

            if (sight == null)
            {
                return NotFound();
            }

            return Ok(sight);
        }
        [HttpPut("{id}")]
        public async Task<IActionResult> PutStats([FromRoute] int id, [FromBody] Statistics stats)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != stats.id)
            {
                return BadRequest();
            }

            _context.Entry(stats).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!StatsExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return Ok();
        }
        private bool StatsExists(int id)
        {
            return _context.statistics.Any(e => e.id == id);
        }
    }
}