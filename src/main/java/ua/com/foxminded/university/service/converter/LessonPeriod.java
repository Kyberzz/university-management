package ua.com.foxminded.university.service.converter;

import java.io.Serializable;
import java.time.LocalTime;

import lombok.Data;

@Data
public class LessonPeriod implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private LocalTime startTime;
    private LocalTime endTime;
}
