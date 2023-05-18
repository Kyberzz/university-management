package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class TimingEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "start_time")
    private LocalTime startTime;
    
    @Column(name = "first_lesson_duration")
    @Convert(converter = DurationConverter.class)
    private Duration firstlessonDuration;
    
    @Column(name = "first_break_duration")
    @Convert(converter = DurationConverter.class)
    private Duration firstBreakDuration;
    
    @Column(name = "second_lesson_duration")
    @Convert(converter = DurationConverter.class)
    private Duration secondLessonDuration;
    
    @Column(name = "second_break_duration")
    @Convert(converter = DurationConverter.class)
    private Duration secondBreakDuration;
    
    @Column(name = "third_lesson_duration")
    @Convert(converter = DurationConverter.class)
    private Duration thirdLessonDuration;
    
    @Column(name = "third_break_duration")
    @Convert(converter = DurationConverter.class)
    private Duration thirdBreakDuration;
    
    @Column(name = "fourth_lesson_duration")
    @Convert(converter = DurationConverter.class)
    private Duration fourthLessonDuration;
    
    @Column(name = "fourth_break_duration")
    @Convert(converter = DurationConverter.class)
    private Duration fourthBreakDuration;
    
    @Column(name = "fifth_lesson_duration")
    @Convert(converter = DurationConverter.class)
    private Duration fifthLessonDuration;
    
    @OneToMany(mappedBy = "timing")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ScheduleEntity> schedules;
    
    public void addSchedule(ScheduleEntity schedule) {
        this.schedules.add(schedule);
        schedule.setTiming(this);
    }
    
    public void removeShcedule(ScheduleEntity schedule) {
        this.schedules.remove(schedule);
        schedule.setTiming(null);
    }
}
