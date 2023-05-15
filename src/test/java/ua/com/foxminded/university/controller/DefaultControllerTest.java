package ua.com.foxminded.university.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.UserEntityMother;

@Transactional
class DefaultControllerTest {
    
    public static final String ERROR_VIEW = "error";
    public static final String BAD_CONTENT = "bad content";
    public static final String AUTHORIZED_EMAIL = "authorized@email";
    
    @PersistenceUnit
    public EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public MockMvc mockMvc;
    
    private UserEntity authorizedUser;
    
    @BeforeTransaction
    void init() {
        authorizedUser = UserEntityMother.complete().email(AUTHORIZED_EMAIL).build();
        
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(authorizedUser);
        
        UserAuthorityEntity userAuthority = UserAuthorityEntity.builder()
                .roleAuthority(RoleAuthority.ROLE_ADMIN)
                .user(authorizedUser)
                .build();
        
        entityManager.persist(userAuthority);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @AfterTransaction
    void cleanUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        UserEntity user = entityManager.find(UserEntity.class, authorizedUser.getId());
        entityManager.remove(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
