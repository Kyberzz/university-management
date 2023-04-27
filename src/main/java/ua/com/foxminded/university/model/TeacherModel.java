package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.UserEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserModel user;
    private List<CourseModel> courseList;
}
