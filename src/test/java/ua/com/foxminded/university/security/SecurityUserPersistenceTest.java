package ua.com.foxminded.university.security;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.Authority;
import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthority;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.UserMother;
import ua.com.foxminded.university.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class SecurityUserPersistenceTest {

    public static final int FIRST_ELEMENT = 1;
    public static final String PASSWORD = "newpass";
    public static final String LAST_NAME = "Lincoln";
    public static final String FIRST_NAME = "Abraham";
    public static final String EMAIL = "newemail@com";
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserDetailsManager userDetailsManager;
    
    @Autowired
    private UserRepository userRepository;
    
    private PasswordEncoder encoder = PasswordEncoderFactories
            .createDelegatingPasswordEncoder();
    private User user;
    private UserAuthority userAuthority;
    
    @BeforeEach
    void init() {
        user = UserMother.complete().build();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        
        userAuthority = UserAuthority.builder()
                                     .roleAuthority(RoleAuthority.ROLE_ADMIN)
                                     .user(user)
                                     .build();
        entityManager.persist(userAuthority);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void deleteUser_ShouldDeleteUserAndUserAuthorityEntities() {
        userDetailsManager.deleteUser(user.getEmail());
        User receivedUser = entityManager.find(User.class, user.getId());
        assertNull(receivedUser);
    }
    
    @Test
    void createUser_ShouldPersistUserEntity() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(EMAIL)
                .disabled(true)
                .password(PASSWORD)
                .passwordEncoder(encoder::encode)
                .roles(Authority.STUDENT.toString())
                .build();
        userDetailsManager.createUser(userDetails);
        Example<User> example = Example.of(user);
        assertTrue(userRepository.exists(example));
    }

    @Test
    void updateUser_ShouldUpdateUserAndUserAuthorityEntities() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .disabled(true)
                .password(PASSWORD)
                .passwordEncoder(encoder::encode)
                .roles(Authority.STUDENT.toString())
                .build();
        
        userDetailsManager.updateUser(userDetails);
        User receivedUser = entityManager.find(User.class, user.getId());
        RoleAuthority roleAuthority = receivedUser.getUserAuthority()
                                                  .getRoleAuthority();
        
        assertEquals(user.getEmail(), receivedUser.getEmail());
        assertTrue(encoder.matches(PASSWORD, receivedUser.getPassword()));
        assertFalse(receivedUser.getEnabled());
        assertEquals(RoleAuthority.ROLE_STUDENT, roleAuthority);
    }
    
    @Test
    void loadUserByUsername_ShouldReturnUserDetailsObjectByEmail() {
        UserDetails userDetails = userDetailsManager.loadUserByUsername(user.getEmail());
        assertEquals(user.getEnabled(), userDetails.isEnabled());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(userAuthority.getRoleAuthority().toString(),
                     userDetails.getAuthorities().iterator().next().getAuthority());
    }
}
