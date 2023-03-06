package ua.com.foxminded.university.objectmother;

import ua.com.foxminded.university.entity.PersonEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.UserEntity;

public class StudentEntityMother {
    
    public static StudentEntity.StudentEntityBuilder complete() {
        
        PersonEntity person = PersonEntityMother.complete().build();
        UserEntity user = UserEntity.builder().person(person).build();
        return StudentEntity.builder().user(user);
    }
}
