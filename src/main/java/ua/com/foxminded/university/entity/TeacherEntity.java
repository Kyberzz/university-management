package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teachers", schema = "university")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToMany
    @JoinTable(
            name = "teacher_course", 
            joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
    @EqualsAndHashCode.Exclude
    private Set<CourseEntity> courses;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private UserEntity user;
}
