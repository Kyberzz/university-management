package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Data;
import lombok.ToString;
import ua.com.foxminded.university.entity.LessonOrder;

@Data
public class TimetableModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private LessonOrder lessonOrder;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private DayOfWeek dayOfWeek;
    
    @ToString.Exclude
    private CourseModel course;
    private GroupModel group;
}
