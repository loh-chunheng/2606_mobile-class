using System.ComponentModel.DataAnnotations;

namespace MySqlEf.Api.DTO;

public class PersonCreate
{
    [Required]
    [MaxLength(100)]
    public string Name {get; set;}

    [Required]
    public int Age {get; set;}

}