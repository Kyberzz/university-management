package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JdbcDaoConfig.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcStudentDaoTest {
    
    private static final String LAST_NAME = "last_name";
    private static final String FIRST_NAME = "first_name";
    private static final String GROUP_ID = "group_id";
    private static final String TEST_INSERT = "test_insert";
    private static final String DATABASE_LAST_NAME_STUDENT = "Smith";
    private static final String DATABASE_FIRST_NAME_STUDENT = "Alex";
    private static final String LAST_NAME_STUDENT = "Deniels";
    private static final String FIRST_NAME_STUDENT = "Jonh";
    private static final int GROUP_ID_NUMBER = 1;
    private static final int STUDENT_ID_NUMBER = 1;
    
   
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    Properties studentQueries;
    
    @Value("${test_insert}")
    private String test_insert;
    
    
//    public int update(T entity);
  //  public int deleteById(int id);
    @Test
    void getGroupByStudentId_GettingDatabaseData_CorrectReceivedData() {
        JdbcStudentDao studentDao = new JdbcStudentDao(jdbcTemplate, studentQueries);
        
        
    }
    
    @Test
    void insert_InsertingStudentToDatabase_CorrectInsertedData() {
        JdbcStudentDao studentDao = new JdbcStudentDao(jdbcTemplate, studentQueries);
        
        StudentEntity student = new StudentEntity();
        student.setFirstName(FIRST_NAME_STUDENT);
        student.setLastName(LAST_NAME_STUDENT);
        student.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        studentDao.insert(student);
        Map<String,Object> databaseStudent = jdbcTemplate.queryForMap(test_insert);
        assertEquals(student.getFirstName(), databaseStudent.get(FIRST_NAME).toString());
        assertEquals(student.getLastName(), databaseStudent.get(LAST_NAME).toString());
        assertEquals(student.getGroup().getId(), databaseStudent.get(GROUP_ID));
    }
    
    @Test
    void getById_GettingStudent_CorrectStudentData() {
        JdbcStudentDao studentDao = new JdbcStudentDao(jdbcTemplate, studentQueries);
        StudentEntity student = studentDao.getById(STUDENT_ID_NUMBER);
        StudentEntity expectedResult = new StudentEntity();
        expectedResult.setId(STUDENT_ID_NUMBER);
        expectedResult.setFirstName(DATABASE_FIRST_NAME_STUDENT);
        expectedResult.setLastName(DATABASE_LAST_NAME_STUDENT);
        expectedResult.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        assertEquals(expectedResult, student);
    }
}
