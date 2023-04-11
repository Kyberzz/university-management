package ua.com.foxminded.university.entitymother;

import java.time.DayOfWeek;
import java.time.LocalTime;

import ua.com.foxminded.university.entity.TimetableEntity;

public class TimetableEntityMother {
    
    private static final String TIMETABLE_DESCRIPTION = "some timetable description";
    private static final LocalTime START_TIME = LocalTime.of(8, 00);
    private static final LocalTime END_TIME = LocalTime.of(9, 45);
    
    public static TimetableEntity.TimetableEntityBuilder complete() {
        return TimetableEntity.builder().dayOfWeek(DayOfWeek.MONDAY)
                                        .description(TIMETABLE_DESCRIPTION)
                                        .startTime(START_TIME)
                                        .endTime(END_TIME);
    }
}
