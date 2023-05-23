package ua.com.foxminded.university.entitymother;

import ua.com.foxminded.university.entity.Person;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.entity.User;

public class StudentMother {
    
    public static Student.StudentBuilder complete() {
        Person person = PersonMother.complete().build();
        User user = User.builder().person(person).build();
        return Student.builder().user(user);
    }
}