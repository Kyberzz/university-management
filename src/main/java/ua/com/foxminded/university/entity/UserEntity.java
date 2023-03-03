package ua.com.foxminded.university.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String password;
    private Boolean enabled;
    
    @OneToOne(mappedBy = "user")
    private TeacherEntity teacher;
    
    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL})
    private StudentEntity student;
    
    @OneToOne(mappedBy = "user")
    private StaffEntity staff;
    
    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL}, 
              orphanRemoval = true)
    private UserAuthorityEntity userAuthority;
}
