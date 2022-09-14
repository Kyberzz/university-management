package ua.com.foxminded.university.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class TimetableModel {

    @NonNull private Integer id;
    private GroupModel group;
    private CourseModel course;
    private long startTime;
    private long endTime;
    private String description;
    private WeekDayModel weekDay;
}
