package ua.com.foxminded.university.model;


import lombok.Data;
import ua.com.foxminded.university.entity.StaffEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TeacherEntity;

@Data
public class UserModel {
    
    private Integer id;
    private String email;
    private String password;
    private Boolean isActive;
    private AuthorityModel authority;

    private TeacherEntity teacher;
    private StudentEntity student;
    private StaffEntity staff;
    
    private String firstName;
    private String lastName;
}
