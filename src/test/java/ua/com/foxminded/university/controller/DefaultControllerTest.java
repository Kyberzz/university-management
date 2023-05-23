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
import ua.com.foxminded.university.entity.UserAuthority;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.UserMother;

@Transactional
class DefaultControllerTest {
    
    public static final String ERROR_VIEW = "error";
    public static final String BAD_CONTENT = "bad content";
    public static final String AUTHORIZED_EMAIL = "authorized@email";
    
    @PersistenceUnit
    public EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public MockMvc mockMvc;
    
    private User authorizedUser;
    
    @BeforeTransaction
    void init() {
        authorizedUser = UserMother.complete().email(AUTHORIZED_EMAIL).build();
        
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(authorizedUser);
        
        UserAuthority userAuthority = UserAuthority.builder()
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
        User user = entityManager.find(User.class, authorizedUser.getId());
        entityManager.remove(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
