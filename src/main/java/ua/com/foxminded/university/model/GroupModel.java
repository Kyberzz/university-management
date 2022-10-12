package ua.com.foxminded.university.model;

import java.util.List;
import lombok.Data;

@Data
public class GroupModel {

    private Integer id;
    private String name;
    private List<TimetableModel> timetableList;
    private List<StudentModel> studentList;
}
