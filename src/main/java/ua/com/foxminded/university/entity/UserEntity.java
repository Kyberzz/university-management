package ua.com.foxminded.university.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "users", schema = "university")
public class UserEntity implements Serializable {
 
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private TeacherEntity teacher;
    
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private StudentEntity student;
    
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private StaffEntity staff;
    
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private AuthorityEntity authority;
}
