package ua.com.foxminded.university.repository.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

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
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.StudentRepository;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryConfigTest.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class StudentRepositoryTest {
    
    private static final String NEW_GROUP_NAME = "rc-58";
    private static final String GROUP_NAME = "rs-01";
    private static final String LAST_NAME_STUDENT = "Smith";
    private static final String FIRST_NAME_STUDENT = "Alex";
    private static final String NEW_LAST_NAME_STUDENT = "Deniels";
    private static final String NEW_FIRST_NAME_STUDENT = "Jonh";
    private static final int NEW_GROUP_ID_NUMBER = 2;
    private static final int GROUP_ID_NUMBER = 1;
    private static final int NEW_STUDENT_ID_NUMBER = 2;
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
        
        GroupEntity secondGroup = new GroupEntity();
        secondGroup.setName(NEW_GROUP_NAME);
        entityManager.persist(secondGroup);
        
        StudentEntity student = new StudentEntity();
        student.setFirstName(FIRST_NAME_STUDENT);
        student.setLastName(LAST_NAME_STUDENT);
        student.setGroup(group);
        entityManager.persist(student);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void update_DeletingGroupIdOfStudent_StudentHasNoGroup() throws RepositoryException {
        StudentEntity student = new StudentEntity();
        student.setId(STUDENT_ID_NUMBER);
        student.setGroup(null);
        
        studentRepository.save(student);
        
        StudentEntity updatedStudent = entityManager.find(StudentEntity.class, STUDENT_ID_NUMBER);
        assertNull(updatedStudent.getGroup());
    }
    
    @Test
    void update_UdatingDatabaseData_DatabaseHasCorrectData() throws RepositoryException {
        StudentEntity student = new StudentEntity();
        student.setId(STUDENT_ID_NUMBER);
        student.setFirstName(NEW_FIRST_NAME_STUDENT);
        student.setLastName(NEW_LAST_NAME_STUDENT);
        GroupEntity group = new GroupEntity();
        group.setId(NEW_GROUP_ID_NUMBER);
        student.setGroup(group);

        studentRepository.save(student);
        
        StudentEntity databaseStudent = entityManager.find(StudentEntity.class, STUDENT_ID_NUMBER);
        assertEquals(NEW_FIRST_NAME_STUDENT, databaseStudent.getFirstName());
        assertEquals(NEW_LAST_NAME_STUDENT, databaseStudent.getLastName());
        assertEquals(STUDENT_ID_NUMBER, databaseStudent.getId());
        assertEquals(NEW_GROUP_ID_NUMBER, databaseStudent.getGroup().getId());
    }
    
    @Test
    void deleteById_DeletingStudentDatabaseData_NoStudentDatabaseData() throws RepositoryException {
        studentRepository.deleteById(STUDENT_ID_NUMBER);
        
        StudentEntity student = new StudentEntity();
        student.setId(STUDENT_ID_NUMBER);
        boolean containStatus = entityManager.contains(student);
        
        assertFalse(containStatus);
    }
    
    @Test
    void insert_InsertingStudentToDatabase_CorrectInsertedData() throws RepositoryException {
        GroupEntity group = new GroupEntity();
        group.setId(GROUP_ID_NUMBER);
        StudentEntity student = new StudentEntity();
        student.setFirstName(NEW_FIRST_NAME_STUDENT);
        student.setLastName(NEW_LAST_NAME_STUDENT);
        student.setGroup(group);
        
        StudentEntity studentWithId = studentRepository.save(student);
        
        StudentEntity databaseStudent = entityManager.find(StudentEntity.class, NEW_STUDENT_ID_NUMBER);
        
        assertEquals(NEW_STUDENT_ID_NUMBER, studentWithId.getId());
        assertEquals(NEW_FIRST_NAME_STUDENT, databaseStudent.getFirstName());
        assertEquals(NEW_LAST_NAME_STUDENT, databaseStudent.getLastName());
        assertEquals(GROUP_ID_NUMBER, databaseStudent.getGroup().getId());
    }
    
    @Test
    void getById_GettingStudent_CorrectStudentData() throws RepositoryException {
        Optional<StudentEntity> student = studentRepository.findById(STUDENT_ID_NUMBER);
        
        assertEquals(STUDENT_ID_NUMBER, student.get().getId());
        assertEquals(FIRST_NAME_STUDENT, student.get().getFirstName());
        assertEquals(LAST_NAME_STUDENT, student.get().getLastName());
        assertEquals(GROUP_ID_NUMBER, student.get().getGroup().getId());
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
