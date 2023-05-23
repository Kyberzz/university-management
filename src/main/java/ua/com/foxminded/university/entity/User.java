package ua.com.foxminded.university.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", schema = "university")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
 
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;
    private Boolean enabled;

    @Embedded
    private Person person;
    
    @OneToOne(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    private Teacher teacher;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    private Student student;
    
    @OneToOne(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    private Staff staff;
    
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, 
              orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private UserAuthority userAuthority;
}
