package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private UserModel user;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CourseModel> courses;
}
