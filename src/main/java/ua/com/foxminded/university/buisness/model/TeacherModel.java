package ua.com.foxminded.university.buisness.model;

import java.util.List;

import lombok.Data;

@Data
public class TeacherModel {

    private Integer id;
    private String firstName;
    private String lastName;
    private List<CourseModel> courseList;
}
