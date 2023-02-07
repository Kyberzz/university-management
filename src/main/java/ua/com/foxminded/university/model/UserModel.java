package ua.com.foxminded.university.model;


import java.io.Serializable;

import lombok.Data;
import ua.com.foxminded.university.entity.StaffEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TeacherEntity;

@Data
public class UserModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
   
    private Integer id;
    private String email;
    private String password;
    private Boolean isActive;
    private UserAuthorityModel userAuthority;

    private TeacherEntity teacher;
    private StudentEntity student;
    private StaffEntity staff;
    
    private String firstName;
    private String lastName;
}
