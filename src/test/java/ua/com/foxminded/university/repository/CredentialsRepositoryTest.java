package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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
    public static final int ROLES_QUANTITY = 2;
    public static final String EMAIL_TWO = "emailTwo";
    public static final String AUTHORITY_USER = "USER";
    public static final String AUTHORITY_ADMIN = "ADMIN";
    public static final String EMAIL_ONE = "emailOne";
    public static final String PASSWORD = "password";
    
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
        CredentialsEntity firstEmailCredentials = new CredentialsEntity();
        firstEmailCredentials.setAuthority(AUTHORITY_ADMIN);
        firstEmailCredentials.setPassword(PASSWORD);
        firstEmailCredentials.setEmail(EMAIL_ONE);
        entityManager.persist(firstEmailCredentials);
        CredentialsEntity secondEmailCredentials = new CredentialsEntity();
        secondEmailCredentials.setAuthority(AUTHORITY_USER);
        secondEmailCredentials.setEmail(EMAIL_TWO);
        secondEmailCredentials.setPassword(PASSWORD);
        entityManager.persist(secondEmailCredentials);
        entityManager.getTransaction().commit();
    }
    
    @Test
    void findByEmail_ShouldReturnCredentialsInstance_WhenEnterEmail() throws RepositoryException {
        CredentialsEntity credentials = credentialsRepository.findByEmail(EMAIL_ONE);
        assertEquals(PASSWORD, credentials.getPassword());
    }
}
