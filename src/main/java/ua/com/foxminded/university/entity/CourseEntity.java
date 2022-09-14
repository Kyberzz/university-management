package ua.com.foxminded.university.entity;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class CourseEntity {
    @NonNull private Integer id;
    private TeacherEntity teacher;
    private String name;
    private String description;
    private List<TimetableEntity> timetableList;
}