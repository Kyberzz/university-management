package ua.com.foxminded.university.entity;

import java.util.List;

import lombok.Data;

@Data
public class GroupEntity {

    private final Integer id;
    private String name;
    private List<TimetableEntity> timetableList;
    private List<StudentEntity> studentList;
}
