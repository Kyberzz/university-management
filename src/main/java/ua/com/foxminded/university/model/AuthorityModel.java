package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import ua.com.foxminded.university.entity.Authorities;

@Data
public class AuthorityModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
   
    private Integer id;
    private Authorities authority;
    private List<Authorities> authorities;
}
