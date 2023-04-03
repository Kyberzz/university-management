package ua.com.foxminded.university.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.foxminded.university.entity.RoleAuthority;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthorityModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
   
    private Integer id;
    private RoleAuthority roleAuthority;
    
    private UserModel user;
    
    @NotNull
    private Authority authority;
}
