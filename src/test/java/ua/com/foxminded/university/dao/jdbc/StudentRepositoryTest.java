package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.config.AppConfigTest;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfigTest.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class StudentRepositoryTest {
    
    private static final String GROUP_NAME = "rs-01";
    private static final String LAST_NAME_STUDENT = "Smith";
    private static final String FIRST_NAME_STUDENT = "Alex";
    private static final String NEW_LAST_NAME_STUDENT = "Deniels";
    private static final String NEW_FIRST_NAME_STUDENT = "Jonh";
    private static final int GROUP_ID_NUMBER = 1;
    private static final int NEW_STUDENT_ID_NUMBER = 4;
    private static final int STUDENT_ID_NUMBER = 1;
   
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    /*
    @Test
    void update_DeletingGroupIdOfStudent_StudentHasNoGroup() throws DaoException {
        StudentJdbcDao studentDao = new StudentJdbcDao(entityManagerFactory);
        StudentEntity student = new StudentEntity();
        student.setId(STUDENT_ID_NUMBER);
        student.setFirstName(FIRST_NAME_STUDENT);
        student.setGroup(new GroupEntity());
        student.setLastName(LAST_NAME_STUDENT);
        
        studentDao.update(student);
        
        StudentEntity updatedStudent = entityManagerFactory.createEntityManager()
                                                           .find(StudentEntity.class, STUDENT_ID_NUMBER);
        assertNull(updatedStudent.getGroup());
    }
    
    @Test
    void update_UdatingDatabaseData_DatabaseHasCorrectData() throws DaoException {
        StudentJdbcDao studentDao = new StudentJdbcDao(entityManagerFactory);
        StudentEntity student = new StudentEntity();
        student.setId(STUDENT_ID_NUMBER);
        student.setFirstName(NEW_FIRST_NAME_STUDENT);
        student.setLastName(NEW_LAST_NAME_STUDENT);
        GroupEntity group = new GroupEntity();
        group.setId(GROUP_ID_NUMBER);
        student.setGroup(group);
        studentDao.update(student);
        
        StudentEntity databaseStudent = entityManagerFactory.createEntityManager()
                                                            .find(StudentEntity.class, STUDENT_ID_NUMBER);
        assertEquals(NEW_FIRST_NAME_STUDENT, databaseStudent.getFirstName());
        assertEquals(NEW_LAST_NAME_STUDENT, databaseStudent.getLastName());
        assertEquals(STUDENT_ID_NUMBER, databaseStudent.getId());
        assertEquals(GROUP_ID_NUMBER, databaseStudent.getGroup().getId());
    }
    
    @Test
    void deleteById_DeletingStudentDatabaseData_NoStudentDatabaseData() throws DaoException {
        StudentJdbcDao studentDao = new StudentJdbcDao(entityManagerFactory);
        studentDao.deleteById(STUDENT_ID_NUMBER);
        StudentEntity student = new StudentEntity();
        student.setId(STUDENT_ID_NUMBER);
        
        boolean containStatus = entityManagerFactory.createEntityManager().contains(student);
        
        assertFalse(containStatus);
    }
    
    @Test
    void getGroupByStudentId_GettingDatabaseData_CorrectReceivedData() throws DaoException {
        StudentJdbcDao studentDao = new StudentJdbcDao(entityManagerFactory);
        StudentEntity studentData = studentDao.getGroupByStudentId(GROUP_ID_NUMBER);
        
        assertEquals(STUDENT_ID_NUMBER, studentData.getId());
        assertEquals(FIRST_NAME_STUDENT, studentData.getFirstName());
        assertEquals(LAST_NAME_STUDENT, studentData.getLastName());
        assertEquals(GROUP_ID_NUMBER, studentData.getGroup().getId());
        assertEquals(GROUP_NAME, studentData.getGroup().getName());
    }
    
    @Test
    void insert_InsertingStudentToDatabase_CorrectInsertedData() throws DaoException {
        StudentJdbcDao studentDao = new StudentJdbcDao(entityManagerFactory);
        StudentEntity student = new StudentEntity();
        student.setFirstName(NEW_FIRST_NAME_STUDENT);
        student.setLastName(NEW_LAST_NAME_STUDENT);
        GroupEntity group = new GroupEntity();
        group.setId(GROUP_ID_NUMBER);
        student.setGroup(group);
        
        StudentEntity studentWithId = studentDao.insert(student);
        
        StudentEntity databaseStudent = entityManagerFactory.createEntityManager()
                                                            .find(StudentEntity.class, NEW_STUDENT_ID_NUMBER);
        
        assertEquals(NEW_STUDENT_ID_NUMBER, studentWithId.getId());
        assertEquals(NEW_FIRST_NAME_STUDENT, databaseStudent.getFirstName());
        assertEquals(NEW_LAST_NAME_STUDENT, databaseStudent.getLastName());
        assertEquals(GROUP_ID_NUMBER, databaseStudent.getGroup().getId());
    }
    */
    
    @Test
    void getById_GettingStudent_CorrectStudentData() throws DaoException {
        StudentRepository studentDao = new StudentRepository(entityManagerFactory);
        StudentEntity student = studentDao.getById(STUDENT_ID_NUMBER);
        
        assertEquals(STUDENT_ID_NUMBER, student.getId());
        assertEquals(FIRST_NAME_STUDENT, student.getFirstName());
        assertEquals(LAST_NAME_STUDENT, student.getLastName());
        assertEquals(GROUP_ID_NUMBER, student.getGroup().getId());
    }
}
