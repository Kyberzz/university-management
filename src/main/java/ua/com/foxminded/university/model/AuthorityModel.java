package ua.com.foxminded.university.model;

import java.util.List;

import lombok.Data;
import ua.com.foxminded.university.entity.Authorities;

@Data
public class AuthorityModel {
    
    private Integer id;
    private Authorities authority;
    private List<Authorities> authorities;
}
