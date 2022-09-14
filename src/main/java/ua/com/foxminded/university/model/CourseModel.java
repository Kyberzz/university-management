package ua.com.foxminded.university.model;

import java.util.List;

import lombok.Data;

@Data
public class CourseModel {
    
    private final Integer id;
    private TeacherModel teacher;
    private String name;
    private String description;
    private List<TimetableModel> timetableList;
}