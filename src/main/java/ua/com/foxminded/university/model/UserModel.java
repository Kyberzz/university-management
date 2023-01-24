package ua.com.foxminded.university.model;

import lombok.Data;

@Data
public class UserModel {
    
    private Integer id;
    private String email;
    private String password;
    private Boolean isActive;
    private TeacherModel teacher;
    private StudentModel student;
    private StaffModel staff;
    private AuthorityModel authority;
}
