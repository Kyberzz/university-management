package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.ToString;

@Data
public class TimetableModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private LessonOrder lessonOrder;
    private LocalTime startTime;
    private LocalDate datestamp;
    private Duration breakDuration;
    private String description;
    
    @ToString.Exclude
    private CourseModel course;
    private GroupModel group;
}
