package ua.com.foxminded.university.objectmother;

import java.time.DayOfWeek;
import java.time.LocalTime;

import ua.com.foxminded.university.entity.TimetableEntity;

public class TimetableEntityMother {
    
    private static final String TIMETABLE_DESCRIPTION = "some timetable description";
    private static final int MINUTE = 0;
    private static final int END_TIME = 9;
    private static final int START_TIME = 8;
    
    public static TimetableEntity.TimetableEntityBuilder complete() {
        return TimetableEntity.builder().dayOfWeek(DayOfWeek.MONDAY)
                                        .description(TIMETABLE_DESCRIPTION)
                                        .endTime(LocalTime.of(END_TIME, MINUTE))
                                        .startTime(LocalTime.of(START_TIME, MINUTE));
    }

}
