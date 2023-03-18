package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "users", schema = "university")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {
 
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;
    private Boolean enabled;

    @Embedded
    private PersonEntity person;
    
    @OneToOne(mappedBy = "user")
    private TeacherEntity teacher;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private StudentEntity student;
    
    @OneToOne(mappedBy = "user")
    private StaffEntity staff;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, 
               orphanRemoval = true)
    private List<UserAuthorityEntity> userAuthorities;
}
