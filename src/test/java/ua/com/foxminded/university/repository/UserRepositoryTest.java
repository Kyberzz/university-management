package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.config.RepositoryTestConfig;
import ua.com.foxminded.university.entity.Authorities;
import ua.com.foxminded.university.entity.AuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = RepositoryTestConfig.class)
@Transactional
@ActiveProfiles("test")
class UserRepositoryTest {
    
    public static final String PASSWORD = "admin";
    public static final String EMAIL = "admin";
    
    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Autowired
    UserRepository userRepository;
    
    @BeforeEach
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
             
        UserEntity user = new UserEntity();
        user.setIsActive(true);
        user.setEmail(EMAIL);
        entityManager.persist(user);
        
        AuthorityEntity authority = new AuthorityEntity();
        authority.setAuthority(Authorities.ADMINISTRATOR);
        authority.setUser(user);
        entityManager.persist(authority);
        entityManager.getTransaction().commit();
    }

    @Test
    void findByEmail_shouldReternUserWithAuthority() {
        UserEntity user = userRepository.findByEmail(EMAIL);
        assertEquals(EMAIL, user.getEmail());
        assertTrue(user.getIsActive());
        assertEquals(PASSWORD, user.getPassword());
    }
}
