package ua.com.foxminded.university.modelmother;

import ua.com.foxminded.university.model.CourseModel;

public class CourseModelMother {
    
    public static final String DESCRIPTION = "some description";
    public static final String NAME = "k-2";
    
    public static CourseModel.CourseModelBuilder complete() {
        return CourseModel.builder().name(NAME)
                                    .description(DESCRIPTION);
    }
}