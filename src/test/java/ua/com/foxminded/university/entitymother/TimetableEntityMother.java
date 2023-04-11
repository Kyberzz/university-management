package ua.com.foxminded.university.entitymother;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

import ua.com.foxminded.university.entity.TimetableEntity;

public class TimetableEntityMother {
    
    private static final String TIMETABLE_DESCRIPTION = "some timetable description";
    private static final LocalTime START_TIME = LocalTime.of(8, 00);
    private static final Duration BREAK_DURATION = Duration.ofMinutes(15);
    
    public static TimetableEntity.TimetableEntityBuilder complete() {
        return TimetableEntity.builder().dayOfWeek(DayOfWeek.MONDAY)
                                        .description(TIMETABLE_DESCRIPTION)
                                        .startTime(START_TIME)
                                        .breakDuration(BREAK_DURATION);
    }
}
