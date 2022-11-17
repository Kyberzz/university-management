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
    private static final String LAST_NAME_STUDENT = "Smith";
    private static final String FIRST_NAME_STUDENT = "Alex";
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
        student.setFirstName(FIRST_NAME_STUDENT);
        student.setLastName(LAST_NAME_STUDENT);
        student.setGroup(group);
        entityManager.persist(student);
        
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void findGroupById_GettingDatabaseData_CorrectReceivedData() throws RepositoryException {
        StudentEntity studentData = studentRepository.findGroupById(GROUP_ID_NUMBER);
        
        assertEquals(STUDENT_ID_NUMBER, studentData.getId());
        assertEquals(FIRST_NAME_STUDENT, studentData.getFirstName());
        assertEquals(LAST_NAME_STUDENT, studentData.getLastName());
        assertEquals(GROUP_ID_NUMBER, studentData.getGroup().getId());
        assertEquals(GROUP_NAME, studentData.getGroup().getName());
    }
}
