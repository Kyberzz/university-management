package org.springframework.security.provisioning;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.UniversityManagement;
import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.UserEntityMother;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.repository.UserRepository;

@SpringBootTest (classes = UniversityManagement.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcUserDetailsManagerSqlTest {
    
    public static final String PASSWORD = "newpass";
    public static final String LAST_NAME = "Lincoln";
    public static final String FIRST_NAME = "Abraham";
    public static final String EMAIL = "newemail@com";
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserDetailsManager userDetailsManager;
    
    private UserEntity user;
    private PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    
    @BeforeEach()
    void setup() {
        user = UserEntityMother.complete().build();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    void deleteUser_ShouldDeleteUserAndUserAuthorityEntities() {
        userDetailsManager.deleteUser(user.getEmail());
        UserEntity receivedUser = entityManager.find(UserEntity.class, user.getId());
        assertNull(receivedUser.getEmail());
        assertNull(receivedUser.getEnabled());
        assertNull(receivedUser.getPassword());
        assertNull(receivedUser.getUserAuthority().getRoleAuthority());
    }
    
    @Test
    void createUser_ShouldPersistUserEntity() {
        UserDetails userDetails = User.builder().username(EMAIL)
                                                .disabled(true)
                                                .password(PASSWORD)
                                                .passwordEncoder(encoder::encode)
                                                .roles(Authority.STUDENT.toString())
                                                .build();
        userDetailsManager.createUser(userDetails);
        Example<UserEntity> example = Example.of(user);
        assertTrue(userRepository.exists(example));
    }

    @Test
    void updateUser_ShouldUpdateUserAndUserAuthorityEntities() {
        UserDetails userDetails = User.builder()
                                      .username(user.getEmail())
                                      .disabled(true)
                                      .password(PASSWORD)
                                      .passwordEncoder(encoder::encode)
                                      .roles(Authority.STUDENT.toString())
                                      .build();
        
        userDetailsManager.updateUser(userDetails);
        UserEntity receivedUser = entityManager.find(UserEntity.class, user.getId());
        
        assertEquals(user.getEmail(), receivedUser.getEmail());
        assertTrue(encoder.matches(PASSWORD, receivedUser.getPassword()));
        assertFalse(receivedUser.getEnabled());
        assertEquals(RoleAuthority.ROLE_STUDENT, 
                     receivedUser.getUserAuthority().getRoleAuthority());
    }
}
