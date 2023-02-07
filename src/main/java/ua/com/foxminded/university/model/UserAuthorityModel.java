package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import ua.com.foxminded.university.entity.Authority;

@Data
public class UserAuthorityModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
   
    private Integer id;
    private Authority authority;
    private UserModel user;
    private List<Authority> authorities;
}
