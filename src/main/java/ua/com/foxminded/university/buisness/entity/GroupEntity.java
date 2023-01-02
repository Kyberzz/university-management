package ua.com.foxminded.university.buisness.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "groups", schema = "university")
@Data
public class GroupEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<TimetableEntity> timetableList;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<StudentEntity> studentList;
}
