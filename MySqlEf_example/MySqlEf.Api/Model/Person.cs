using System.ComponentModel.DataAnnotations;

namespace MySqlEf.Api.Model;

public class Person
{
    public int Id {get; set;}

    [Required]
    [MaxLength(100)]
    public string Name {get; set;}

    [Required]
    public int Age {get; set;}

}