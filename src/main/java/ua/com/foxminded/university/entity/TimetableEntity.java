package ua.com.foxminded.university.entity;


import lombok.Data;
import lombok.NonNull;

@Data
public class TimetableEntity {

    @NonNull private Integer id;
    private GroupEntity group;
    private CourseEntity course;
    private long startTime;
    private long endTime;
    private String description;
    private WeekDayEntity weekDay;
}
