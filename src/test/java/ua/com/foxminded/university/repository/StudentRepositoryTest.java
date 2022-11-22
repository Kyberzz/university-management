package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.config.RepositoryConfigTest;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryConfigTest.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentRepositoryTest {
    
    private static final String GROUP_NAME = "rs-01";
    private static final String STUDENT_LAST_NAME = "Smith";
    private static final String STUDENT_FIRST_NAME = "Alex";
    private static final int GROUP_ID_NUMBER = 1;
    private static final int STUDENT_ID_NUMBER = 1;
   
    @PersistenceContext
    private EntityManager entityManager;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @BeforeEach 
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        GroupEntity group = new GroupEntity();
        group.setName(GROUP_NAME);
        entityManager.persist(group);
        entityManager.flush();
        
        StudentEntity student = new StudentEntity();
        student.setFirstName(STUDENT_FIRST_NAME);
        student.setLastName(STUDENT_LAST_NAME);
        student.setGroup(group);
        entityManager.persist(student);
        
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void findGroupById_GettingDatabaseData_CorrectReceivedData() throws RepositoryException {
        StudentEntity studentData = studentRepository.findGroupById(GROUP_ID_NUMBER);
        
        assertEquals(STUDENT_ID_NUMBER, studentData.getId());
        assertEquals(STUDENT_FIRST_NAME, studentData.getFirstName());
        assertEquals(STUDENT_LAST_NAME, studentData.getLastName());
        assertEquals(GROUP_ID_NUMBER, studentData.getGroup().getId());
        assertEquals(GROUP_NAME, studentData.getGroup().getName());
    }
    
    @Test
    void findById_GettingStudentById_CorrectRetrievedData() throws RepositoryException {
        StudentEntity student = studentRepository.findById(STUDENT_ID_NUMBER);
        assertEquals(STUDENT_ID_NUMBER, student.getId());
        assertEquals(STUDENT_FIRST_NAME, student.getFirstName());
        assertEquals(STUDENT_LAST_NAME, student.getLastName());
    }
}
