package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.config.TestAppConfig;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestAppConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class StudentJdbcDaoTest {
    
    private static final String SELECT_STUDENT_BY_ID = "test.selectStudentById";
    private static final String GROUP_NAME = "rs-01";
    private static final String LAST_NAME_COLUMN = "last_name";
    private static final String FIRST_NAME_COLUMN = "first_name";
    private static final String GROUP_ID = "group_id";
    private static final String SELECT_STUDENT_BY_NAME = "test.selectStudentByName";
    private static final String LAST_NAME_STUDENT = "Smith";
    private static final String FIRST_NAME_STUDENT = "Alex";
    private static final String NEW_LAST_NAME_STUDENT = "Deniels";
    private static final String NEW_FIRST_NAME_STUDENT = "Jonh";
    private static final String STUDENT_ID = "id";
    private static final int GROUP_ID_NUMBER = 1;
    private static final int STUDENT_ID_NUMBER = 1;
    
   
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private Environment queries;
    
    @Test
    void update_UdatingDatabaseData_DatabaseHasCorrectData() {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, queries);
        StudentEntity student = new StudentEntity();
        student.setFirstName(NEW_FIRST_NAME_STUDENT);
        student.setLastName(NEW_LAST_NAME_STUDENT);
        student.setId(STUDENT_ID_NUMBER);
        student.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        studentDao.update(student);
        Map<String, Object> databaseStudent = jdbcTemplate.queryForMap(
                queries.getProperty(SELECT_STUDENT_BY_ID), 
                STUDENT_ID_NUMBER);
        assertEquals(NEW_FIRST_NAME_STUDENT, databaseStudent.get(FIRST_NAME_COLUMN));
        assertEquals(NEW_LAST_NAME_STUDENT, databaseStudent.get(LAST_NAME_COLUMN));
        assertEquals(STUDENT_ID_NUMBER, databaseStudent.get(STUDENT_ID));
        assertEquals(GROUP_ID_NUMBER, databaseStudent.get(GROUP_ID));
    }
    
    @Test
    void deleteById_DeletingStudentDatabaseData_NoStudentDatabaseData() throws SQLException {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, queries);
        studentDao.deleteById(STUDENT_ID_NUMBER);
        jdbcTemplate.query(
                queries.getProperty(SELECT_STUDENT_BY_ID),
                preperadStatement -> preperadStatement.setInt(1, STUDENT_ID_NUMBER),
                resultSet -> {
                    assertTrue(!resultSet.next());
                });
    }
    
    @Test
    void getGroupByStudentId_GettingDatabaseData_CorrectReceivedData() {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, queries);
        StudentEntity studentData = studentDao.getGroupByStudentId(GROUP_ID_NUMBER);
        
        assertEquals(STUDENT_ID_NUMBER, studentData.getId());
        assertEquals(FIRST_NAME_STUDENT, studentData.getFirstName());
        assertEquals(LAST_NAME_STUDENT, studentData.getLastName());
        assertEquals(GROUP_ID_NUMBER, studentData.getGroup().getId());
        assertEquals(GROUP_NAME, studentData.getGroup().getName());
    }
    
    @Test
    void insert_InsertingStudentToDatabase_CorrectInsertedData() {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, queries);
        StudentEntity student = new StudentEntity();
        student.setFirstName(NEW_FIRST_NAME_STUDENT);
        student.setLastName(NEW_LAST_NAME_STUDENT);
        student.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        studentDao.insert(student);
        Map<String,Object> databaseStudent = jdbcTemplate.queryForMap(
                queries.getProperty(SELECT_STUDENT_BY_NAME));
        
        assertEquals(NEW_FIRST_NAME_STUDENT, databaseStudent.get(FIRST_NAME_COLUMN).toString());
        assertEquals(NEW_LAST_NAME_STUDENT, databaseStudent.get(LAST_NAME_COLUMN).toString());
        assertEquals(GROUP_ID_NUMBER, databaseStudent.get(GROUP_ID));
    }
    
    @Test
    void getById_GettingStudent_CorrectStudentData() {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, queries);
        StudentEntity student = studentDao.getById(STUDENT_ID_NUMBER);
        StudentEntity expectedResult = new StudentEntity();
        expectedResult.setId(STUDENT_ID_NUMBER);
        expectedResult.setFirstName(FIRST_NAME_STUDENT);
        expectedResult.setLastName(LAST_NAME_STUDENT);
        expectedResult.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        assertEquals(expectedResult, student);
    }
}
