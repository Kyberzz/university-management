package ua.com.foxminded.university.dtomother;

import ua.com.foxminded.university.dto.PersonDTO;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.dto.UserDTO;

public class StudentDTOMother {
    
    public static StudentDTO.StudentDTOBuilder complete() {
        PersonDTO person = PersonDTOMother.complete().build();
        UserDTO user = UserDTOMother.complete().person(person).build();
        return StudentDTO.builder().user(user);
    }
}
