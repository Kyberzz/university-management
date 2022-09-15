package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.StudentMapper;
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
    private static final String GROUP_ID_COLUMN = "group_id";
    private static final String SELECT_STUDENT_BY_NAME = "test.selectStudentByName";
    private static final String LAST_NAME_STUDENT = "Smith";
    private static final String FIRST_NAME_STUDENT = "Alex";
    private static final String NEW_LAST_NAME_STUDENT = "Deniels";
    private static final String NEW_FIRST_NAME_STUDENT = "Jonh";
    private static final String STUDENT_ID = "id";
    private static final int GROUP_ID_NUMBER = 1;
    private static final int STUDENT_ID_NUMBER = 1;
    private static final int NO_STUDENT_ID = 0;
    private static final Integer NO_GROUP_ID = null;
    
   
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private Environment queries;
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private GroupMapper groupMapper;
    
    @Test
    void update_DeletingGroupIdOfStudent_StudentHasNoGroup() throws DaoException {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, queries, studentMapper, groupMapper);
        StudentEntity student = new StudentEntity(STUDENT_ID_NUMBER);
        student.setFirstName(FIRST_NAME_STUDENT);
        student.setGroup(new GroupEntity(NO_GROUP_ID));
        student.setLastName(LAST_NAME_STUDENT);
        studentDao.update(student);
        String sqlSelectStudentById = queries.getProperty(SELECT_STUDENT_BY_ID);
        Map<String, Object> updatedStudent = jdbcTemplate.queryForMap(sqlSelectStudentById, 
                                                                      STUDENT_ID_NUMBER);
        assertNull(updatedStudent.get(GROUP_ID_COLUMN));
    }
    
    @Test
    void update_UdatingDatabaseData_DatabaseHasCorrectData() throws DaoException {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, queries, studentMapper, groupMapper);
        StudentEntity student = new StudentEntity(STUDENT_ID_NUMBER);
        student.setFirstName(NEW_FIRST_NAME_STUDENT);
        student.setLastName(NEW_LAST_NAME_STUDENT);
        student.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        studentDao.update(student);
        String sqlSelectStudentById = queries.getProperty(SELECT_STUDENT_BY_ID);
        Map<String, Object> databaseStudent = jdbcTemplate.queryForMap(sqlSelectStudentById, 
                                                                       STUDENT_ID_NUMBER);
        assertEquals(NEW_FIRST_NAME_STUDENT, databaseStudent.get(FIRST_NAME_COLUMN));
        assertEquals(NEW_LAST_NAME_STUDENT, databaseStudent.get(LAST_NAME_COLUMN));
        assertEquals(STUDENT_ID_NUMBER, databaseStudent.get(STUDENT_ID));
        assertEquals(GROUP_ID_NUMBER, databaseStudent.get(GROUP_ID_COLUMN));
    }
    
    @Test
    void deleteById_DeletingStudentDatabaseData_NoStudentDatabaseData() throws DaoException {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, queries, studentMapper, groupMapper);
        studentDao.deleteById(STUDENT_ID_NUMBER);
        String sqlSelectStudentById = queries.getProperty(SELECT_STUDENT_BY_ID);
        jdbcTemplate.query(sqlSelectStudentById,
                           preperadStatement -> preperadStatement.setInt(1, STUDENT_ID_NUMBER),
                           resultSet -> {
                               assertTrue(!resultSet.next());
                           });
    }
    
    @Test
    void getGroupByStudentId_GettingDatabaseData_CorrectReceivedData() throws DaoException {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, queries, studentMapper, groupMapper);
        StudentEntity studentData = studentDao.getGroupByStudentId(GROUP_ID_NUMBER);
        
        assertEquals(STUDENT_ID_NUMBER, studentData.getId());
        assertEquals(FIRST_NAME_STUDENT, studentData.getFirstName());
        assertEquals(LAST_NAME_STUDENT, studentData.getLastName());
        assertEquals(GROUP_ID_NUMBER, studentData.getGroup().getId());
        assertEquals(GROUP_NAME, studentData.getGroup().getName());
    }
    
    @Test
    void insert_InsertingStudentToDatabase_CorrectInsertedData() throws DaoException {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, queries, studentMapper, groupMapper);
        StudentEntity student = new StudentEntity(NO_STUDENT_ID);
        student.setFirstName(NEW_FIRST_NAME_STUDENT);
        student.setLastName(NEW_LAST_NAME_STUDENT);
        student.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        studentDao.insert(student);
        String sqlSelectStudentByName =  queries.getProperty(SELECT_STUDENT_BY_NAME);
        Map<String,Object> databaseStudent = jdbcTemplate.queryForMap(sqlSelectStudentByName);
        
        assertEquals(NEW_FIRST_NAME_STUDENT, databaseStudent.get(FIRST_NAME_COLUMN).toString());
        assertEquals(NEW_LAST_NAME_STUDENT, databaseStudent.get(LAST_NAME_COLUMN).toString());
        assertEquals(GROUP_ID_NUMBER, databaseStudent.get(GROUP_ID_COLUMN));
    }
    
    @Test
    void getById_GettingStudent_CorrectStudentData() throws DaoException {
        StudentJdbcDao studentDao = new StudentJdbcDao(jdbcTemplate, queries, studentMapper, groupMapper);
        StudentEntity student = studentDao.getById(STUDENT_ID_NUMBER);
        StudentEntity expectedResult = new StudentEntity(STUDENT_ID_NUMBER);
        expectedResult.setFirstName(FIRST_NAME_STUDENT);
        expectedResult.setLastName(LAST_NAME_STUDENT);
        expectedResult.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        assertEquals(expectedResult, student);
    }
}
