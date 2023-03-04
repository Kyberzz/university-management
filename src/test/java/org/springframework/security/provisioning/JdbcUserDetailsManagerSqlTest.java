package org.springframework.security.provisioning;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.UniversityManagementApplication;
import ua.com.foxminded.university.config.RepositoryTestConfig;
import ua.com.foxminded.university.config.ServiceConfig;
import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.security.SecurityConfig;

//@Transactional
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {RepositoryTestConfig.class, SecurityConfig.class})
@SpringBootTest(classes = UniversityManagementApplication.class)
@ActiveProfiles("test")
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcUserDetailsManagerSqlTest {
    
    public static final String NEW_PASSWORD = "{noop}newpass";
    public static final String PASSWORD = "{noop}pass";
    public static final String LAST_NAME = "Lincoln";
    public static final String FIRST_NAME = "Abraham";
    public static final String EMAIL = "email@com";
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private UserDetailsManager userDetailsManager;
    
    private UserEntity userEntity;
    
    @BeforeEach()
    void setup() {
        userEntity = new UserEntity();
        userEntity.setEmail(EMAIL);
        userEntity.setEnabled(true);
        userEntity.setFirstName(FIRST_NAME);
        userEntity.setLastName(LAST_NAME);
        userEntity.setPassword(PASSWORD);
        
        UserAuthorityEntity userAuthorityEntity = new UserAuthorityEntity();
        userAuthorityEntity.setUser(userEntity);
        userAuthorityEntity.setRoleAuthority(RoleAuthority.ROLE_ADMIN);
        
        userEntity.setUserAuthority(userAuthorityEntity);
        
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(userEntity);
        entityManager.getTransaction().commit();
    }

    @Test
    void updateUser_shouldUpdatePartialOfUserEntity() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails userDetails = User.builder().username(EMAIL)
                                                .disabled(true)
                                                .password(NEW_PASSWORD)
                                                .passwordEncoder(encoder::encode)
                                                .roles(Authority.STUDENT.toString())
                                                .build();
        userDetailsManager.updateUser(userDetails);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UserEntity updatedUser = entityManager.find(UserEntity.class, userEntity.getId());
        
        assertEquals(EMAIL, updatedUser.getEmail());
        assertFalse(updatedUser.getEnabled());
        assertEquals(NEW_PASSWORD, updatedUser.getPassword());
        assertEquals(RoleAuthority.ROLE_STUDENT, 
                     updatedUser.getUserAuthority().getRoleAuthority());
        
//        fail("Not yet implemented");
    }

}
