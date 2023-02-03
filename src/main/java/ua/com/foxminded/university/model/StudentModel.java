package ua.com.foxminded.university.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class StudentModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int id;
    private GroupModel group;
    private String firstName;
    private String lastName;
    private UserModel user;
    
    public boolean hasGroup() {
        return group !=null && group.getId() != null;
    }
}
