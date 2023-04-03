package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class TeacherModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    
    @EqualsAndHashCode.Exclude
    private UserModel user;
    private List<CourseModel> courseList;
}
