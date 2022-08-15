package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.entity.StudentEntity;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/test-config.xml")
@ExtendWith(MockitoExtension.class)
class JdbcStudentDaoTest {
    
    private DataSource dataSource;
    
    public JdbcStudentDaoTest(DataSource dataSource) {
        this.dataSource = dataSource;
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
     //   when(properties.get(anyString())).thenReturn("select * from university.students where id=?");
        jdbcTemplate.setDataSource(dataSource);
        StudentEntity student = jdbcStudentDao.getStudentById(1);
        System.out.println(student.toString());
        assertEquals('1', student.getId());
    }
}
