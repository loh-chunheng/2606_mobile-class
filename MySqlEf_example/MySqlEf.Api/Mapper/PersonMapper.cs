using MySqlEf.Api.DTO;
using MySqlEf.Api.Model;

namespace MySqlEf.Api.Mapper;

public static class PersonMapper
{
    public static PersonRead ToPersonRead(this Person person)
    {
        return new PersonRead
        {
            Id = person.Id,
            Name = person.Name,
            Age = person.Age
        };
    }

    public static PersonUpdate ToPersonUpdate(this Person person)
    {
        return new PersonUpdate
        {
            Id = person.Id,
            Name = person.Name,
            Age = person.Age
        };
        
    }

    public static Person ToPerson(this PersonCreate personCreate)
    {
        return new Person
        {
            Name = personCreate.Name,
            Age = personCreate.Age
        };
    }
    
}