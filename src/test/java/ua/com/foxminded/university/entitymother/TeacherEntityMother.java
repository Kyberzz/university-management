package ua.com.foxminded.university.entitymother;

import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.UserEntity;

public class TeacherEntityMother {
    
    public static TeacherEntity.TeacherEntityBuilder complete() {
        UserEntity user = UserEntityMother.complete().build();
        return TeacherEntity.builder().user(user);
    }
}