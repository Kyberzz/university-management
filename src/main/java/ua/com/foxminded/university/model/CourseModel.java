package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    @NotNull
    private String name;
    private String description;
    
    @EqualsAndHashCode.Exclude
    private Set<TeacherModel> teachers;
    
    @EqualsAndHashCode.Exclude
    private Set<TimetableModel> timetables;
}