package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class TimetableEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    
    @OneToMany(mappedBy = "timetable")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<LessonTimingEntity> lessonTiming;
    
    public void addLessonTiming(LessonTimingEntity lessonTiming) {
        this.lessonTiming.add(lessonTiming);
        lessonTiming.setTimetable(this);
    }
    
    public void removeLessonTiming(LessonTimingEntity lessonTiming) {
        this.lessonTiming.remove(lessonTiming);
        lessonTiming.setTimetable(null);
    }
}
