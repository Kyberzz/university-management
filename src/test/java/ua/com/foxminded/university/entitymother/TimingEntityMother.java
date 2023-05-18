package ua.com.foxminded.university.entitymother;

import java.time.Duration;
import java.time.LocalTime;

import ua.com.foxminded.university.entity.TimingEntity;

public class TimingEntityMother {
    
    public static final LocalTime START_TIME = LocalTime.of(8, 00);
    public static final Duration FOURTH_BREAK_DURATION = Duration.ofMinutes(10);
    public static final Duration THIRD_BREAK_DURATION = Duration.ofMinutes(20);
    public static final Duration SECOND_BREAK_DURATION = Duration.ofMinutes(45);
    public static final Duration FIRST_BREAK_DURATION = Duration.ofMinutes(15);
    public static final Duration LESSON_DURATION = Duration.ofMinutes(90);
    
    public static TimingEntity.TimingEntityBuilder complete() {
        return TimingEntity.builder().startTime(START_TIME)
                                     .lessonDuration(LESSON_DURATION)
                                     .firstBreakDuration(FIRST_BREAK_DURATION)
                                     .secondBreakDuration(SECOND_BREAK_DURATION)
                                     .thirdBreakDuration(THIRD_BREAK_DURATION)
                                     .fourthBreakDuration(FOURTH_BREAK_DURATION);
    }
}
