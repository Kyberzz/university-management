package ua.com.foxminded.university.model;

import java.io.Serializable;

import lombok.Data;
import ua.com.foxminded.university.entity.RoleAuthority;

@Data
public class UserAuthorityModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
   
    private Integer id;
    private RoleAuthority roleAuthority;
    private UserModel user;
    private Authority authority;
}
