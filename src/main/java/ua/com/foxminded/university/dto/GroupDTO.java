package ua.com.foxminded.university.dto;

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
public class GroupDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private StudentDTO student;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<StudentDTO> students;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<LessonDTO> lessons;
}