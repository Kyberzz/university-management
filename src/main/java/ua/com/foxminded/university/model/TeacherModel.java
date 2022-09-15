package ua.com.foxminded.university.model;

import java.util.List;

import lombok.Data;

@Data
public class TeacherModel {

    private final Integer id;
    private String firstName;
    private String lastName;
    private List<CourseModel> courseList;
}
