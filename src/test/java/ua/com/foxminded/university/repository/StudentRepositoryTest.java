package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.entitymother.UserMother.EMAIL;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.GroupMother;
import ua.com.foxminded.university.entitymother.UserMother;

@DataJpaTest
@ActiveProfiles("test")
class StudentRepositoryTest {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    private Student student;
    private Group group;
    private User user;
    
    @BeforeEach 
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        group = GroupMother.complete().build();
        entityManager.persist(group);
        
        user = UserMother.complete().build();
        entityManager.persist(user);
        
        student = Student.builder()
                         .group(group)
                         .user(user)
                         .build();
        entityManager.persist(student);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @AfterEach
    void cleanUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Student persistedStudent = entityManager.find(Student.class, student.getId());
        entityManager.remove(persistedStudent);
        User persistedUser = entityManager.find(User.class, user.getId());
        entityManager.remove(persistedUser);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void findByUserEmail_ShouldReturnStudent() {
        Student persistedStudent = studentRepository.findByUserEmail(EMAIL.toString());
        
        assertEquals(student, persistedStudent);
    }
    
    @Test
    void findGroupById_ShouldRetrunGroupByStudentId() {
        Student persistedStudent = studentRepository.findGroupById(student.getId());
        assertEquals(group.getId(), persistedStudent.getGroup().getId());
    }
    
    @Test
    void findById_ShouldReturnStudentObjectWithId() {
        Student persistedStudent = studentRepository.findById(student.getId());
        assertEquals(student.getId(), persistedStudent.getId());
    }
}
