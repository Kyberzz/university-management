package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.config.RepositoryTestConfig;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.objectmother.GroupEntityMother;
import ua.com.foxminded.university.objectmother.StudentEntityMother;
import ua.com.foxminded.university.objectmother.UserEntityMother;

@ActiveProfiles("test")
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryTestConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentRepositoryTest {
    
    private static final String EMAIL = "email@com";
    private static final String GROUP_NAME = "rs-01";
    private static final String STUDENT_LAST_NAME = "Smith";
    private static final String STUDENT_FIRST_NAME = "Alex";
    private static final int GROUP_ID_NUMBER = 1;
    private static final int STUDENT_ID_NUMBER = 1;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    private StudentEntity student;
    
    @BeforeEach 
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        GroupEntity group = GroupEntityMother.complete().build();
        entityManager.persist(group);
        
        UserEntity user = UserEntityMother.complete().build();
        entityManager.persist(user);
        
        student = StudentEntityMother.complete()
                                     .group(group)
                                     .user(user)
                                     .build();
        entityManager.persist(student);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void findGroupById_GettingDatabaseData_CorrectReceivedData() throws RepositoryException {
        StudentEntity persistedStudent = studentRepository.findGroupById(GROUP_ID_NUMBER);
        
        assertEquals(student.getId(), persistedStudent.getId());
        assertEquals(student.getUser().getPersonEntity().getFirstName(), 
                     persistedStudent.getUser().getPersonEntity().getFirstName());
        assertEquals(student.getUser().getPersonEntity().getLastName(), 
                     persistedStudent.getUser().getPersonEntity().getLastName());
        assertEquals(student.getGroup().getId(), persistedStudent.getGroup().getId());
        assertEquals(GROUP_NAME, persistedStudent.getGroup().getName());
    }
    
    @Test
    void findById_GettingStudentById_CorrectRetrievedData() throws RepositoryException {
        StudentEntity persistedStudent = studentRepository.findById(STUDENT_ID_NUMBER);
        assertEquals(STUDENT_ID_NUMBER, persistedStudent.getId());
        assertEquals(STUDENT_FIRST_NAME, persistedStudent.getUser().getFirstName());
        assertEquals(STUDENT_LAST_NAME, persistedStudent.getUser().getLastName());
    }
}
