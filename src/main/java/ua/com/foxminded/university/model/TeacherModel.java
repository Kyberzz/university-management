package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class TeacherModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String firstName;
    private String lastName;
    private List<CourseModel> courseList;
}
