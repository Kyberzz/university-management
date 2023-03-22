package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class GroupModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private StudentModel student;
    private List<StudentModel> studentList;
    private List<TimetableModel> timetableList;
}
