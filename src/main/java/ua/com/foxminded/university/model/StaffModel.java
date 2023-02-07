package ua.com.foxminded.university.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class StaffModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String firstName;
    private String lastName;
    private String position;
    private UserAuthorityModel credentials;
}
