package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.com.foxminded.university.converter.DurationConverter;

@Entity
@Table(name = "timings", schema = "university")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonTimingEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "start_time")
    private LocalTime startTime;
    
    @Column(name = "lesson_duration")
    @Convert(converter = DurationConverter.class)
    private Duration lessonDuration;
    
    @Column(name = "break_duration")
    @Convert(converter = DurationConverter.class)
    private Duration breakDuration;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    @ToString.Exclude
    private TimetableEntity timetable;
    
    @OneToMany(mappedBy = "lessonTiming")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ScheduleEntity> schedules;
    
    public void addSchedule(ScheduleEntity schedule) {
        this.schedules.add(schedule);
        schedule.setLessonTiming(this);
    }
    
    public void removeShcedule(ScheduleEntity schedule) {
        this.schedules.remove(schedule);
        schedule.setLessonTiming(null);
    }
}
