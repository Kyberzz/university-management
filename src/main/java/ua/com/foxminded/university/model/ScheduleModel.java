package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.com.foxminded.university.entity.LessonOrder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private LocalDate datestamp;
    private LessonOrder lessonOrder;
    private String description;
    
    @ToString.Exclude
    private TimingModel timing;
    
    @ToString.Exclude
    private CourseModel course;
    
    @ToString.Exclude
    private GroupModel group;
}
