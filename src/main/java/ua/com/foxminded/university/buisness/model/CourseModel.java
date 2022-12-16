package ua.com.foxminded.university.buisness.model;

import java.util.List;

import lombok.Data;

@Data
public class CourseModel {
    
    private Integer id;
    private TeacherModel teacher;
    private String name;
    private String description;
    private List<TimetableModel> timetableList;
}