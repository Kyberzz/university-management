package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
@ContextConfiguration("/test-config.xml")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcStudentDaoTest {
    private static final String LAST_NAME = "Smith";
    private static final String FIRST_NAME = "Alex";
    private static final int GROUP_ID = 1;
    private static final int STUDENT_ID = 1;
    
   
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    Properties properties;
    
    @Test
    void getStudentById_GettingStudent_CorrectStudentData() {
        JdbcStudentDao jdbcStudentDao = new JdbcStudentDao(jdbcTemplate, properties);
        StudentEntity student = jdbcStudentDao.getStudentById(1);
        StudentEntity expectedResult = new StudentEntity();
        expectedResult.setId(STUDENT_ID);
        expectedResult.setFirstName(FIRST_NAME);
        expectedResult.setLastName(LAST_NAME);
        expectedResult.setGroup(new GroupEntity(GROUP_ID));
        assertEquals(expectedResult, student);
    }
}
