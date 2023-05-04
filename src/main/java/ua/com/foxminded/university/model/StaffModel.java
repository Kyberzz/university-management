package ua.com.foxminded.university.model;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
public class StaffModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    @ToString.Exclude
    private UserModel user;
}