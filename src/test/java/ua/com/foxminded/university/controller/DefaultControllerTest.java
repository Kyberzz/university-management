package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_ADMIN;
import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_TEACHER;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.entity.UserAuthority;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.UserMother;

@Transactional
class DefaultControllerTest {
    
    public static final String TIMETABLE_NAME = "first";
    public static final String ERROR_VIEW = "error";
    public static final String BAD_CONTENT = "bad content";
    public static final String ADMIN_EMAIL = "admin@email";
    public static final String TEACHER_EMAIL = "teacher@email";
    
    @PersistenceUnit
    public EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public MockMvc mockMvc;
    
    public User adminUser;
    public User teacherUser;
    
    @BeforeTransaction
    void init() {
        adminUser = UserMother.complete().email(ADMIN_EMAIL).build();
        teacherUser = UserMother.complete().email(TEACHER_EMAIL).build();
        
        
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(adminUser);
        entityManager.persist(teacherUser);
        
        UserAuthority adminUserAuthority = UserAuthority.builder()
                .roleAuthority(ROLE_ADMIN)
                .user(adminUser)
                .build();
        UserAuthority teacherUserAuthority = UserAuthority.builder()
                .roleAuthority(ROLE_TEACHER)
                .user(teacherUser)
                .build();
        
        entityManager.persist(teacherUserAuthority);
        entityManager.persist(adminUserAuthority);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @AfterTransaction
    void cleanUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        User persistedAdminUser = entityManager.find(User.class, adminUser.getId());
        entityManager.remove(persistedAdminUser);
        User persistedTeacherUser = entityManager.find(User.class, teacherUser.getId());
        entityManager.remove(persistedTeacherUser);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
