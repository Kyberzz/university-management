package ua.com.foxminded.university.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class TimetableModel implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String name;
}
