package ua.com.foxminded.university.model;

import lombok.Data;

@Data
public class StudentModel {

    private int id;
    private GroupModel group;
    private String firstName;
    private String lastName;
    private UserModel user;
    
    
    public void setGroup(GroupModel group) {
        if (hasGroup(group)) {
            this.group = group;
        } else {
            this.group = null;
        }
    }
     
    private boolean hasGroup(GroupModel group) {
        return group.getId() != null;
    }
}
