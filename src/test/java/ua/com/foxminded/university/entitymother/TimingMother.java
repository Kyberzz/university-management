package ua.com.foxminded.university.entitymother;

import java.time.Duration;
import java.time.LocalTime;

import ua.com.foxminded.university.entity.Timing;

public class TimingMother {
    
    public static final LocalTime START_TIME = LocalTime.of(8, 00);
    public static final Duration BREAK_DURATION = Duration.ofMinutes(15);
    public static final Duration LESSON_DURATION = Duration.ofMinutes(90);
    
    public static Timing.TimingBuilder complete() {
        return Timing.builder().startTime(START_TIME)
                               .lessonDuration(LESSON_DURATION)
                               .breakDuration(BREAK_DURATION);
    }
}
