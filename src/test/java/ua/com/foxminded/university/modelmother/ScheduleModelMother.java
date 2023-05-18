package ua.com.foxminded.university.modelmother;

import static ua.com.foxminded.university.entitymother.ScheduleEntityMother.*;

import ua.com.foxminded.university.entity.LessonOrder;
import ua.com.foxminded.university.model.ScheduleModel;

public class ScheduleModelMother {
    
    public static ScheduleModel.ScheduleModelBuilder complete() {
        return ScheduleModel.builder().datestamp(DATE)
                                      .description(SCHEDULE_DESCRIPTION)
                                      .lessonOrder(LessonOrder.FIRST_LESSON);
    }
}
