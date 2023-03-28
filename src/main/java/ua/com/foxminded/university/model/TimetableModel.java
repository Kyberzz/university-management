package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Data;
import ua.com.foxminded.university.entity.LessonOrder;
import ua.com.foxminded.university.service.converter.LessonPeriod;

@Data
public class TimetableModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private LessonOrder lessonOrder;
    private LessonPeriod lessonPeriod;
//    private LocalTime startTime;
//    private LocalTime endTime;
    private String description;
    private DayOfWeek dayOfWeek;
    private CourseModel course;
    private GroupModel group;
}
