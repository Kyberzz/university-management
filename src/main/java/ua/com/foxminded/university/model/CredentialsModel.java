package ua.com.foxminded.university.model;

import java.util.List;

import lombok.Data;

@Data
public class CredentialsModel {
    
    private Integer id;
    private String email;
    private String password;
    private String authority;
    private TeacherModel teacher;
    private StudentModel sutdent;
    private StaffModel staff;
    private List<String> authorities;
}
