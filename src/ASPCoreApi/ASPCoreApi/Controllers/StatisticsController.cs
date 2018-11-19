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
    [Route("api/[controller]")]
    [ApiController]
    public class StatisticsController : ControllerBase
    {
        private readonly DatabaseContext _context;

        public StatisticsController(DatabaseContext context)
        {
            _context = context;
        }
        // GET: api/Statistics/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetStatistics([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var statistics = await _context.stats.FindAsync(id);

            if (statistics == null)
            {
                return NotFound();
            }

            return Ok(statistics);
        }

        // PUT: api/Statistics/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutStatistics([FromRoute] int id, [FromBody] Statistics statistics)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != statistics.Id)
            {
                return BadRequest();
            }

            _context.Entry(statistics).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!StatisticsExists(id))
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
        private bool StatisticsExists(int id)
        {
            return _context.stats.Any(e => e.Id == id);
        }
    }
}