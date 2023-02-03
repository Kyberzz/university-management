package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Data;

@Data
public class TimetableModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private GroupModel group;
    private CourseModel course;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private DayOfWeek dayOfWeek;
}
