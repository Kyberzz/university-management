package ua.com.foxminded.university.entitymother;

import ua.com.foxminded.university.entity.CourseEntity;

public class CourseEntityMother {
    
    private static final String COURSE_NAME = "Programming";
    private static final String COURSE_DESCRIPTION = "some description";
    
    public static CourseEntity.CourseEntityBuilder complete() {
        return CourseEntity.builder().description(COURSE_DESCRIPTION)
                                     .name(COURSE_NAME);
    }
}