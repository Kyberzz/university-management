package ua.com.foxminded.university.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class TimetableDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String name;
}
