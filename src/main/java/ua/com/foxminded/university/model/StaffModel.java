package ua.com.foxminded.university.model;

import lombok.Data;

@Data
public class StaffModel {
    
    private Integer id;
    private String firstName;
    private String lastName;
    private String position;
    private CredentialsModel credentials;
}
