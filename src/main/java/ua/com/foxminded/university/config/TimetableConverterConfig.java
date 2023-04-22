package ua.com.foxminded.university.config;

import java.time.LocalTime;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties
@Getter
@Setter
public class TimetableConverterConfig {
    
    private LocalTime firstLessonStartTime;
    private int averageLessonMinutesInterval;
}
