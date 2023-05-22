package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "lessons", schema = "university")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate datestamp;
    private String description;
    
    @Column(name = "lesson_order")
    private Integer lessonOrder;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @ToString.Exclude
    private GroupEntity group;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    private CourseEntity course;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "timetable_id")
    @ToString.Exclude
    private TimetableEntity timetable;
}
