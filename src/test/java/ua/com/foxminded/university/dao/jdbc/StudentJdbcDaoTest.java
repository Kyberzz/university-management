package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JdbcDaoTestConfig.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class StudentJdbcDaoTest {
    
    private static final String SELECT_STUDENT_BY_ID = "selectStudentById";
    private static final String GROUP_NAME = "rs-01";
    private static final String LAST_NAME = "last_name";
    private static final String FIRST_NAME = "first_name";
    private static final String GROUP_ID = "group_id";
    private static final String SELECT_STUDENT_BY_NAME = "selectStudentByName";
    private static final String DATABASE_LAST_NAME_STUDENT = "Smith";
    private static final String DATABASE_FIRST_NAME_STUDENT = "Alex";
    private static final String LAST_NAME_STUDENT = "Deniels";
    private static final String FIRST_NAME_STUDENT = "Jonh";
    private static final String STUDENT_ID = "id";
    private static final int GROUP_ID_NUMBER = 1;
    private static final int STUDENT_ID_NUMBER = 1;
    
   
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    Properties studentQueries;
    
    @Autowired
    Properties testStudentQueries;
    
    @Test
    void update_UdatingDatabaseData_DatabaseHasCorrectData() {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, studentQueries);
        StudentEntity student = new StudentEntity();
        student.setFirstName(FIRST_NAME_STUDENT);
        student.setLastName(LAST_NAME_STUDENT);
        student.setId(STUDENT_ID_NUMBER);
        student.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        studentDao.update(student);
        Map<String, Object> databaseStudent = jdbcTemplate.queryForMap(
                testStudentQueries.getProperty(SELECT_STUDENT_BY_ID), 
                STUDENT_ID_NUMBER);
        assertEquals(student.getFirstName(), databaseStudent.get(FIRST_NAME));
        assertEquals(student.getLastName(), databaseStudent.get(LAST_NAME));
        assertEquals(student.getId(), databaseStudent.get(STUDENT_ID));
        assertEquals(student.getGroup().getId(), databaseStudent.get(GROUP_ID));
    }
    
    @Test
    void deleteById_DeletingStudentDatabaseData_NoStudentDatabaseData() throws SQLException {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, studentQueries);
        studentDao.deleteById(STUDENT_ID_NUMBER);
        jdbcTemplate.query(
                testStudentQueries.getProperty(SELECT_STUDENT_BY_ID),
                preperadStatement -> preperadStatement.setInt(1, STUDENT_ID_NUMBER),
                resultSet -> {
                    assertTrue(!resultSet.next());
                });
    }
    
    @Test
    void getGroupByStudentId_GettingDatabaseData_CorrectReceivedData() {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, studentQueries);
        StudentEntity student = studentDao.getGroupByStudentId(GROUP_ID_NUMBER);
        StudentEntity expectedResult = new StudentEntity();
        GroupEntity group = new GroupEntity();
        group.setId(GROUP_ID_NUMBER);
        group.setName(GROUP_NAME);
        expectedResult.setGroup(group);
        expectedResult.setId(STUDENT_ID_NUMBER);
        expectedResult.setFirstName(DATABASE_FIRST_NAME_STUDENT);
        expectedResult.setLastName(DATABASE_LAST_NAME_STUDENT);
        assertEquals(expectedResult, student);
    }
    
    @Test
    void insert_InsertingStudentToDatabase_CorrectInsertedData() {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, studentQueries);
        StudentEntity student = new StudentEntity();
        student.setFirstName(FIRST_NAME_STUDENT);
        student.setLastName(LAST_NAME_STUDENT);
        student.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        studentDao.insert(student);
        Map<String,Object> databaseStudent = jdbcTemplate.queryForMap(
                testStudentQueries.getProperty(SELECT_STUDENT_BY_NAME));
        assertEquals(student.getFirstName(), databaseStudent.get(FIRST_NAME).toString());
        assertEquals(student.getLastName(), databaseStudent.get(LAST_NAME).toString());
        assertEquals(student.getGroup().getId(), databaseStudent.get(GROUP_ID));
    }
    
    @Test
    void getById_GettingStudent_CorrectStudentData() {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, studentQueries);
        StudentEntity student = studentDao.getById(STUDENT_ID_NUMBER);
        StudentEntity expectedResult = new StudentEntity();
        expectedResult.setId(STUDENT_ID_NUMBER);
        expectedResult.setFirstName(DATABASE_FIRST_NAME_STUDENT);
        expectedResult.setLastName(DATABASE_LAST_NAME_STUDENT);
        expectedResult.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        assertEquals(expectedResult, student);
    }
}
