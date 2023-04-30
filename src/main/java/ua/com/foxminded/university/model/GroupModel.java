package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class GroupModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private StudentModel student;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<StudentModel> studentList;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TimetableModel> timetableList;
}
