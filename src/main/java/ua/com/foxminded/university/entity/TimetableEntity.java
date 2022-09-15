package ua.com.foxminded.university.entity;


import lombok.Data;

@Data
public class TimetableEntity {

    private final Integer id;
    private GroupEntity group;
    private CourseEntity course;
    private long startTime;
    private long endTime;
    private String description;
    private WeekDayEntity weekDay;
}
