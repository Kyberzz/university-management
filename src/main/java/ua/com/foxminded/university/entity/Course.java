package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "courses", schema = "university")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    
    @ManyToMany(mappedBy = "courses")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Teacher> teachers;
    
    @OneToMany(mappedBy = "course")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Lesson> lessons;
    
    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.setCourse(null);
    }
    
    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.getCourse().setLessons(lessons);
    }
    
    public void addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getCourses().add(this);
    }
    
    public void removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getCourses().remove(this);
    }
}
