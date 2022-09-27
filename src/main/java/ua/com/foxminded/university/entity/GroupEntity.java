package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "groups")
@Data
public class GroupEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<TimetableEntity> timetableList;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<StudentEntity> studentList;
}
