package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "groups", schema = "university")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToMany(mappedBy = "groups")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Lesson> lessons;
  
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Student> students;
}
