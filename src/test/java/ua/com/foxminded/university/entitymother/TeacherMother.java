package ua.com.foxminded.university.entitymother;

import ua.com.foxminded.university.entity.Teacher;
import ua.com.foxminded.university.entity.User;

public class TeacherMother {
    
    public static Teacher.TeacherBuilder complete() {
        User user = UserMother.complete().build();
        return Teacher.builder().user(user);
    }
}