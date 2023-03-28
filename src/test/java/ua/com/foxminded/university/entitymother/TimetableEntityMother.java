package ua.com.foxminded.university.entitymother;

import java.time.DayOfWeek;

import ua.com.foxminded.university.entity.LessonOrder;
import ua.com.foxminded.university.entity.TimetableEntity;

public class TimetableEntityMother {
    
    private static final String TIMETABLE_DESCRIPTION = "some timetable description";
    
    public static TimetableEntity.TimetableEntityBuilder complete() {
        return TimetableEntity.builder().dayOfWeek(DayOfWeek.MONDAY)
                                        .description(TIMETABLE_DESCRIPTION)
                                        .lessonOrder(LessonOrder.FIFTH_LESSON)
                                        .lessonOrder(LessonOrder.FIRST_LESSON);
    }
}
