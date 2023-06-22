package ua.com.foxminded.university.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
public class StaffDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    @ToString.Exclude
    private UserDTO user;
}
