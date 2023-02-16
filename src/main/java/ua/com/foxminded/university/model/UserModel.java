package ua.com.foxminded.university.model;


import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
   
    @Email
    private String email;
    
    @NotNull
    private String password;
    
    @NotNull
    private Boolean enabled;

    private String firstName;
    private String lastName;
    
    private UserAuthorityModel userAuthority;
    private TeacherModel teacher;
    private StudentModel student;
    private StaffModel staff;
    
    
    public boolean hasStaff() {
        return staff != null;
    }
    
    public boolean hasStudent() {
        return student != null;
    }
    
    public boolean hasTeacher() {
        return teacher != null;
    }
    
    public boolean hasUserAuthority() {
        return userAuthority != null;
    }
}
