package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CourseModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private TeacherModel teacher;
    private String name;
    private String description;
    private List<TimetableModel> timetableList;
}