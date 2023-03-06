package ua.com.foxminded.university.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Column(name = "first_name")
    String firstName;
    
    @Column(name = "last_name")
    String lastName;
}
