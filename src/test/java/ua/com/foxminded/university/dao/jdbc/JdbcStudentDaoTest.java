package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.entity.StudentEntity;

@ExtendWith(MockitoExtension.class)
class JdbcStudentDaoTest {
    
    
  //  private Properties studentQueries;
    private DataSource embeddedDatabase;
    
    public JdbcStudentDaoTest(
           // Properties studentQueries, 
            DataSource embeddedDatabase) {
      //  this.studentQueries = studentQueries;
        this.embeddedDatabase = embeddedDatabase;
    }

    @InjectMocks
    JdbcStudentDao jdbcStudentDao;
    
    @Mock
    JdbcTemplate jdbcTemplate;
    
    @Mock
    Properties properties;
    
    /*
    @BeforeEach
    void init() {
        when(properties).thenReturn(studentQueries);
        jdbcTemplate.setDataSource(embeddedDatabase);
    }
    */
    
    @Test
    void getStudentById_GettingStudent_CorrectData() {
        when(properties.get(anyString())).thenReturn("select * from university.students where id=?");
        jdbcTemplate.setDataSource(embeddedDatabase);
        StudentEntity student = jdbcStudentDao.getStudentById(1);
        System.out.println(student.toString());
        assertEquals('1', student.getId());
    }
}
