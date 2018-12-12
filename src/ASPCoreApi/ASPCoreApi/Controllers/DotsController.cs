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
    public class DotController : ControllerBase
    {
        private readonly DatabaseContext _context;

        public DotController(DatabaseContext context)
        {
            _context = context;
        }

        // GET: /Dots
        [HttpGet]
        public IEnumerable<Dot> GetDots()
        {
            return _context.dots;
        }

        // GET: /Dots/id
        [HttpGet("{id}")]
        public async Task<IActionResult> GetDot([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var dot = await _context.dots.FindAsync(id);

            if (dot == null)
            {
                return NotFound();
            }

            return Ok(dot);
        }

        // PUT: /Dots/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutDot([FromRoute] int id, [FromBody] Dot dot)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != dot.id)
            {
                return BadRequest();
            }

            _context.Entry(dot).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!dotExists(id))
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

        // POST: /Dots
        [HttpPost]
        public async Task<IActionResult> PostDot([FromBody] Dot dot)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.dots.Add(dot);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetDot", new { id = dot.id }, dot);
        }
        // POST: /Dots
        [HttpPost("postarray")]
        public async Task<IActionResult> PostDot([FromBody] Dot[] dotlist)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            foreach (Dot dot in dotlist)
            {
                _context.dots.Add(dot);
            }

            int x = await _context.SaveChangesAsync();

            return Created(string.Empty, x);
        }
        // DELETE: Dots/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteDot([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var dot = await _context.dots.FindAsync(id);
            if (dot == null)
            {
                return NotFound();
            }

            _context.dots.Remove(dot);
            await _context.SaveChangesAsync();

            return Ok(dot);
        }

        private bool dotExists(int id)
        {
            return _context.dots.Any(e => e.id == id);
        }
    }
}