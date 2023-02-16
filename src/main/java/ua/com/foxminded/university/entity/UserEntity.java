package ua.com.foxminded.university.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class UserEntity implements Serializable {
 
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "username")
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
