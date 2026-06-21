using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Http;
using MySqlEf.Api.Model;
using MySqlEf.Api.DTO;
using MySqlEf.Api.Mapper;

namespace MySqlEf.Api.Controllers;

[Route("api/person")]
[ApiController]
public class PersonController : ControllerBase
{
    private readonly AppDbContext _context;

    public PersonController(AppDbContext context)
    {
        _context = context;
    }
    
    [HttpGet()]
    public async Task<IActionResult> GetPeople()
    {
        try
        {
            var people = await _context.Person.AsNoTracking().ToListAsync();
            var peopleRead = people.Select(p => p.ToPersonRead()); // mapped to DTO
            return Ok(peopleRead);
        }
        catch (Exception ex)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, ex.Message);
        }
    }

    [HttpGet("{id}", Name = "GetPerson")]
    public async Task<IActionResult> GetPerson(int id)
    {
        try
        {
            var person = await _context.Person.FindAsync(id);

            // check for not found and return status code accordingly
            if (person is null)
            {
                return NotFound($"Person with id: {id} does not found.");
            }
            return Ok(person.ToPersonRead());
        }
        catch (Exception ex)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, ex.Message);
        }
    }

    [HttpPost]
    public async Task<IActionResult> CreatePerson(PersonCreate personCreate)
    {
        try
        {
            var person = personCreate.ToPerson();
            _context.Person.Add(person);
            await _context.SaveChangesAsync();

            return CreatedAtRoute("GetPerson", new { id = person.Id }, person.ToPersonRead()); 
        }
        catch (Exception ex)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, ex.Message);
        }
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> UpdatePerson(int id, [FromBody] PersonUpdate personUpdate)
    {
        if (id != personUpdate.Id)
        {
            return BadRequest("Ids mismatch");
        }
        try
        {
            var isPersonExists = _context.Person.AsNoTracking().Any(p => p.Id == id);
            // check for not found and return status code accordingly
            if (!isPersonExists)
            {
                return NotFound($"Person with id: {id} does not found.");
            }
            var person = personUpdate.ToPerson();
            _context.Person.Update(person);
            await _context.SaveChangesAsync();
            return NoContent(); // returns 204 NoContent status code
        }
        catch (Exception ex)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, ex.Message);
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeletePerson(int id)
    {
        try
        {
            var person = await _context.Person.FindAsync(id);
            // check for not found and return status code accordingly
            if (person == null)
            {
                return NotFound($"Person with id: {id} does not found.");
            }
            _context.Person.Remove(person);
            await _context.SaveChangesAsync();
            return NoContent(); // returns 204 NoContent status code
        }
        catch (Exception ex)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, ex.Message);
        }
    }

}
