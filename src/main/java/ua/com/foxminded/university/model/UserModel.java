package ua.com.foxminded.university.model;


import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    
    @Email
    private String email;

    @NotNull
    private Boolean enabled;
    private String password;
    
    private PersonModel person;
    
    @NotNull
    @EqualsAndHashCode.Exclude
    private UserAuthorityModel userAuthority;
    
    @EqualsAndHashCode.Exclude
    private TeacherModel teacher;
    
    @EqualsAndHashCode.Exclude
    private StudentModel student;
    
    @EqualsAndHashCode.Exclude
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
