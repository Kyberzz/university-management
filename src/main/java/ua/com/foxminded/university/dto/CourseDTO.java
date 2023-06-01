package ua.com.foxminded.university.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

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
public class CourseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    @NotNull
    private String name;
    private String description;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<TeacherDTO> teachers;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<LessonDTO> timetables;
}
