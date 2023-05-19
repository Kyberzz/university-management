package ua.com.foxminded.university.modelmother;

import static ua.com.foxminded.university.entitymother.LessonEntityMother.*;

import ua.com.foxminded.university.model.LessonModel;

public class LessonModelMother {

    public static final int FIRST_LESSON = 1;
    public static LessonModel.LessonModelBuilder complete() {
        return LessonModel.builder().datestamp(DATE)
                                      .description(SCHEDULE_DESCRIPTION)
                                      .lessonOrder(FIRST_LESSON);
    }
}
