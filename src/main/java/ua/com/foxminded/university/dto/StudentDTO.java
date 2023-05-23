package ua.com.foxminded.university.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int id;
    
    @NotNull
    @ToString.Exclude
    private UserDTO user;
    
    @ToString.Exclude
    private GroupDTO group;
    
    public boolean hasUser() {
        return user != null;
    }
    
    public boolean hasGroup() {
        return group !=null && group.getId() != null;
    }
}
