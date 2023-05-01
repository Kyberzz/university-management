package ua.com.foxminded.university.modelmother;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import ua.com.foxminded.university.model.LessonOrder;
import ua.com.foxminded.university.model.LessonStartTime;
import ua.com.foxminded.university.model.TimetableModel;

public class TimetableModelMother {
    
    public static final LocalTime LOCAL_TIME = LessonStartTime.TIME_8_00
                                                              .getRepresentation();
    public static final String DESCRIPTION = "some desctiption";
    public static final LocalDate LOCAL_DATE = LocalDate.of(2023, 05, 01);
    public static final Duration DURATION = Duration.ofMinutes(15);
    
    public static TimetableModel.TimetableModelBuilder complete() {
        return TimetableModel.builder().breakDuration(DURATION)
                                       .datestamp(LOCAL_DATE)
                                       .description(DESCRIPTION)
                                       .lessonOrder(LessonOrder.FIRST_LESSON)
                                       .startTime(LOCAL_TIME);
    }

}
