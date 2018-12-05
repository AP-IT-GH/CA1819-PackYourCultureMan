using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using ASPCoreApi.Models;
using Microsoft.AspNetCore.Authorization;

namespace ASPCoreApi.Controllers
{

    [Route("[controller]")]
    [ApiController]
    public class StreetController : ControllerBase
    {
        private readonly DatabaseContext _context;

        public StreetController(DatabaseContext context)
        {
            _context = context;
        }

        // GET: /Streets
        [HttpGet]
        public IEnumerable<Streets> GetStreets()
        {
            return _context.streets;
        }

        // GET: /Streets/id
        [HttpGet("{id}")]
        public async Task<IActionResult> GetStreet([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var street = await _context.streets.FindAsync(id);

            if (street == null)
            {
                return NotFound();
            }

            return Ok(street);
        }

        // PUT: /Streets/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutDot([FromRoute] int id, [FromBody] Streets street)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != street.id)
            {
                return BadRequest();
            }

            _context.Entry(street).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!streetExists(id))
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

        // POST: /Streets
        [HttpPost]
        public async Task<IActionResult> PostStreet([FromBody] Streets street)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.streets.Add(street);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetStreet", new { id = street.id }, street);
        }
        // POST: /Streets
        [HttpPost("postarray")]
        public async Task<IActionResult> PostStreet([FromBody] Streets[] streetList)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            foreach (Streets street in streetList)
            {
                _context.streets.Add(street);
            }

            int x = await _context.SaveChangesAsync();

            return Created(string.Empty, x);
        }
        // DELETE: /Streets/id
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteStreet([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var street = await _context.streets.FindAsync(id);
            if (street == null)
            {
                return NotFound();
            }

            _context.streets.Remove(street);
            await _context.SaveChangesAsync();

            return Ok(street);
        }

        private bool streetExists(int id)
        {
            return _context.streets.Any(e => e.id == id);
        }
    }
}