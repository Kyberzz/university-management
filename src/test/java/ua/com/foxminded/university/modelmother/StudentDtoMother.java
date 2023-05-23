package ua.com.foxminded.university.modelmother;

import ua.com.foxminded.university.dto.PersonDTO;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.dto.UserDTO;

public class StudentDtoMother {
    
    public static StudentDTO.StudentDTOBuilder complete() {
        PersonDTO person = PersonDtoMother.complete().build();
        UserDTO user = UserDtoMother.complete().person(person).build();
        return StudentDTO.builder().user(user);
    }
}
