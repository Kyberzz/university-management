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
import ua.com.foxminded.university.entity.CredentialsEntity;
import ua.com.foxminded.university.exception.RepositoryException;

@ActiveProfiles("test")
@Transactional
@ContextConfiguration(classes = RepositoryTestConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class CredentialsRepositoryTest {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private CredentialsRepository credentialsRepository;
    
    @BeforeEach
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
        entityManager.getTransaction().begin();
        CredentialsEntity credentials = new CredentialsEntity();
        credentials.setPassword("admin");
        credentials.setEmail("apache");
        entityManager.persist(credentials);
        entityManager.getTransaction().commit();
    }

    @Test
    void getByEmail_ShouldReturnCredentialsInstance_WhenEnterEmail() throws RepositoryException {
        CredentialsEntity credentials = credentialsRepository.findByEmail("apache");
        
        assertEquals("admin", credentials.getPassword());
    }
}
