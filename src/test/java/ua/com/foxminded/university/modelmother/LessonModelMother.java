package ua.com.foxminded.university.modelmother;

import static ua.com.foxminded.university.entitymother.LessonEntityMother.*;

import ua.com.foxminded.university.entity.LessonOrder;
import ua.com.foxminded.university.model.LessonModel;

public class LessonModelMother {
    
    public static LessonModel.LessonModelBuilder complete() {
        return LessonModel.builder().datestamp(DATE)
                                      .description(SCHEDULE_DESCRIPTION)
                                      .lessonOrder(LessonOrder.FIRST_LESSON);
    }
}
