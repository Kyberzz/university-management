package ua.com.foxminded.university.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private LocalDate datestamp;
    private String description;
    
    @NotBlank
    private Integer lessonOrder;
    
    @ToString.Exclude
    private TimetableDTO timetable;
    
    @ToString.Exclude
    private CourseDTO course;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<GroupDTO> groups;
    
    @ToString.Exclude
    @NotBlank
    private TeacherDTO teacher;
    
    @ToString.Exclude
    @NotBlank
    private GroupDTO group;
    private LocalTime startTime;
    private LocalTime endTime;
    
    public boolean hasTeacher() {
        return teacher != null;
    }
    
    public boolean hasTimetable() {
        return timetable != null;
    }
}
