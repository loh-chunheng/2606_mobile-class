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


}
