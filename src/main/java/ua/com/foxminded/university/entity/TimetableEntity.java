
package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Convert;
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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.com.foxminded.university.converter.DurationConverter;

@Entity
@Table(name = "timetables", schema = "university")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "start_time")
    private LocalTime startTime;
    
    @Column(name = "break_duration")
    @Convert(converter = DurationConverter.class)
    private Duration breakDuration;
    
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @EqualsAndHashCode.Exclude
    private GroupEntity group;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @EqualsAndHashCode.Exclude
    private CourseEntity course;
    
    @Column(name = "datestamp")
    private LocalDate datestamp;
}
