package ua.com.foxminded.university.service.converter;

import java.time.LocalTime;

import lombok.Data;

@Data
public class LessonPeriod {
    
    private LocalTime startTime;
    private LocalTime endTime;
}
