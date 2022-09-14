package ua.com.foxminded.university.entity;

import java.util.List;

import lombok.Data;

@Data
public class TeacherEntity {

    private final Integer id;
    private String firstName;
    private String lastName;
    private List<CourseEntity> courseList;
}
