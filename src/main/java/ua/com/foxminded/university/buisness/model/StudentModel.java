package ua.com.foxminded.university.buisness.model;

import lombok.Data;

@Data
public class StudentModel {

    private int id;
    private GroupModel group;
    private String firstName;
    private String lastName;
}
