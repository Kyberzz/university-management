package ua.com.foxminded.university.entity;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class GroupEntity {

    @NonNull private Integer id;
    private String name;
    private List<TimetableEntity> timetableList;
    private List<StudentEntity> studentList;
}
