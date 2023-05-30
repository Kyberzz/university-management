package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "lessons", schema = "university")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate datestamp;
    private String description;
    
    @Column(name = "lesson_order")
    private Integer lessonOrder;

    @ManyToMany
    @JoinTable(
            schema = "university",
            name = "lesson_group",
            joinColumns = @JoinColumn(name = "lesson_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set <Group> groups;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    private Course course;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "timetable_id")
    @ToString.Exclude
    private Timetable timetable;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @ToString.Exclude
    private Teacher teacher;
    
    public void addGroup(Group group) {
        this.groups.add(group);
        group.getLessons().add(this);
    }
    
    public void removeGroup(Group group) {
        this.groups.remove(group);
        group.getLessons().remove(this);
    }
    
    public boolean hasTimetable() {
        return timetable != null;
    }
}
