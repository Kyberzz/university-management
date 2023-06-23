package ua.com.foxminded.university.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPersonDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
}
