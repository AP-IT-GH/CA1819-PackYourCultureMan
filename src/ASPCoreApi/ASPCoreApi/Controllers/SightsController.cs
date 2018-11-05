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
    public class SightsController : ControllerBase
    {
        private readonly DatabaseContext _context;

        public SightsController(DatabaseContext context)
        {
            _context = context;
        }

        // GET: api/Sights
        [HttpGet]
        public IEnumerable<Sight> Getsights()
        {
            return _context.sights;
        }

        // GET: api/Sights/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetSight([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var sight = await _context.sights.FindAsync(id);

            if (sight == null)
            {
                return NotFound();
            }

            return Ok(sight);
        }

        // PUT: api/Sights/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutSight([FromRoute] int id, [FromBody] Sight sight)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != sight.id)
            {
                return BadRequest();
            }

            _context.Entry(sight).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!SightExists(id))
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

        // POST: api/Sights
        [HttpPost]
        public async Task<IActionResult> PostSight([FromBody] Sight sight)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.sights.Add(sight);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetSight", new { id = sight.id }, sight);
        }

        // DELETE: api/Sights/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteSight([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var sight = await _context.sights.FindAsync(id);
            if (sight == null)
            {
                return NotFound();
            }

            _context.sights.Remove(sight);
            await _context.SaveChangesAsync();

            return Ok(sight);
        }

        private bool SightExists(int id)
        {
            return _context.sights.Any(e => e.id == id);
        }
    }
}