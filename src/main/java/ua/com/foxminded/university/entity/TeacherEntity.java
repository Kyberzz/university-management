package ua.com.foxminded.university.entity;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class TeacherEntity {

    @NonNull private Integer id;
    private String firstName;
    private String lastName;
    private List<CourseEntity> courseList;
}
