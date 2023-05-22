package ua.com.foxminded.university.model;

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
import ua.com.foxminded.university.entity.TimetableEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimingModel implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private LocalTime startTime;
    private Duration lessonDuration;
    private Duration breakDuration;
    
    private TimetableEntity timetable;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<LessonModel> schedules;
}
