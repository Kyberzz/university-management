package ua.com.foxminded.university.model;

import java.time.Duration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LessonBreakDuration {
    
    DURATION_15_MIN(15), DURATION_45_MIN(45);
    
    private Duration duration;

    private LessonBreakDuration(long value) {
        duration = Duration.ofMinutes(value);
    }
}
