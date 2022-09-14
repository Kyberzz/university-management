package ua.com.foxminded.university.entity;

import java.util.List;

import lombok.Data;

@Data
public class CourseEntity {
    
    private final Integer id;
    private TeacherEntity teacher;
    private String name;
    private String description;
    private List<TimetableEntity> timetableList;
}