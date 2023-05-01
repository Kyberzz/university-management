package ua.com.foxminded.university.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class StudentModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int id;
    
    @NotNull
    private UserModel user;
    private GroupModel group;
    
    public boolean hasUser() {
        return user != null;
    }
    
    public boolean hasGroup() {
        return group !=null && group.getId() != null;
    }
}