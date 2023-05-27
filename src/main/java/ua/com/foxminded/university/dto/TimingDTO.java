package ua.com.foxminded.university.dto;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimingDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    @NotNull
    private LocalTime startTime;
    
    private Duration lessonDuration;
    
    private Duration breakDuration;
    
    private TimetableDTO timetable;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<LessonDTO> schedules;
    
    private LocalTime endTime;
}
