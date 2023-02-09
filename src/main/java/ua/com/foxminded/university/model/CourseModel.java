package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CourseModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String name;
    private String description;
    private TeacherModel teacher;
    private List<TimetableModel> timetableList;
}