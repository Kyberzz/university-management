package ua.com.foxminded.university.modelmother;

import ua.com.foxminded.university.dto.CourseDTO;

public class CourseDtoMother {
    
    public static final String DESCRIPTION = "some description";
    public static final String NAME = "k-2";
    
    public static CourseDTO.CourseDTOBuilder complete() {
        return CourseDTO.builder().name(NAME)
                                  .description(DESCRIPTION);
    }
}
