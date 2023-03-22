package ua.com.foxminded.university.model;


import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    @NotNull
    @Email
    private String email;

    @NotNull
    private Boolean enabled;
    private String password;
    private PersonModel person;
    
    @NotNull
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
