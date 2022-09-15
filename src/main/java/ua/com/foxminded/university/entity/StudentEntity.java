package ua.com.foxminded.university.entity;


import lombok.Data;

@Data
public class StudentEntity {

    private final int id;
    private GroupEntity group;
    private String firstName;
    private String lastName;
}
