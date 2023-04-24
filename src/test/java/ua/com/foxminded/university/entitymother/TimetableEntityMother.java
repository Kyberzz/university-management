package ua.com.foxminded.university.entitymother;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import ua.com.foxminded.university.entity.TimetableEntity;

public class TimetableEntityMother {
    
    private static final String TIMETABLE_DESCRIPTION = "some timetable description";
    private static final LocalDate DATE = LocalDate.of(2023, 4, 24);
    private static final LocalTime START_TIME = LocalTime.of(8, 00);
    private static final Duration BREAK_DURATION = Duration.ofMinutes(15);
    
    public static TimetableEntity.TimetableEntityBuilder complete() {
        return TimetableEntity.builder().datestamp(DATE)
                                        .description(TIMETABLE_DESCRIPTION)
                                        .startTime(START_TIME)
                                        .breakDuration(BREAK_DURATION);
    }
}
