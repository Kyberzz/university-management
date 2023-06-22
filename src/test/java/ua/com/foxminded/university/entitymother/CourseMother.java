package ua.com.foxminded.university.entitymother;

import ua.com.foxminded.university.entity.Course;

public class CourseMother {
    
    private static final String COURSE_NAME = "Programming";
    private static final String COURSE_DESCRIPTION = "some description";
    
    public static Course.CourseBuilder complete() {
        return Course.builder().description(COURSE_DESCRIPTION)
                               .name(COURSE_NAME);
    }
}
