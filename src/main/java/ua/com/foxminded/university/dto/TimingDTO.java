package ua.com.foxminded.university.dto;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.com.foxminded.university.entity.Timetable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimingDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private LocalTime startTime;
    private Duration lessonDuration;
    private Duration breakDuration;
    
    private Timetable timetable;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<LessonDTO> schedules;
    
    private LocalTime endTime;
}
