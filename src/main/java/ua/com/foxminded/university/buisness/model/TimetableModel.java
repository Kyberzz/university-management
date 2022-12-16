package ua.com.foxminded.university.buisness.model;

import lombok.Data;

@Data
public class TimetableModel {

    private Integer id;
    private GroupModel group;
    private CourseModel course;
    private long startTime;
    private long endTime;
    private String description;
    private WeekDayModel weekDay;
}
