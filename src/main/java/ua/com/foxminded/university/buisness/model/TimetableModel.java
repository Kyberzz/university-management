package ua.com.foxminded.university.buisness.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Data;

@Data
public class TimetableModel {

    private Integer id;
    private GroupModel group;
    private CourseModel course;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private DayOfWeek dayOfWeek;
}
