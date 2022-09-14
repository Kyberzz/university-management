package ua.com.foxminded.university.entity;


import lombok.Data;
import lombok.NonNull;

@Data
public class StudentEntity {

    @NonNull private int id;
    private GroupEntity group;
    private String firstName;
    private String lastName;
}
