package ua.com.foxminded.university.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    
    @Email
    private String email;

    @NotNull
    private Boolean enabled;
    private String password;
    
    private PersonDTO person;
    
    @NotNull
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserAuthorityDTO userAuthority;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private TeacherDTO teacher;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private StudentDTO student;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private StaffDTO staff;
    
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