using Microsoft.EntityFrameworkCore;
using MySqlEf.Api.Model;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
string dbConnectionString = builder.Configuration.GetConnectionString("default") ?? 
throw new InvalidOperationException("Connection string 'default' not found.");
builder.Services.AddDbContext<AppDbContext>(options => options.UseMySQL(dbConnectionString));

builder.Services.AddControllers();
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddOpenApi();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
