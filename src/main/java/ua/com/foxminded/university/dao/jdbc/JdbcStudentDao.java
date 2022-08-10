package ua.com.foxminded.university.dao.jdbc;

import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

public class JdbcStudentDao implements StudentDao {
    
    private static final String GET_STUDENT_BY_ID = "getStudentById";
    private static final String LAST_NAME = "last_name";
    private static final String FIRST_NAME = "first_name";
    private static final String GROUP_ID = "group_id";
    private static final String ID = "id";
    
    private Properties studentQuery;
    private JdbcTemplate jdbcTemplate;
    
    public JdbcStudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void setStudentQuery(Properties properties) {
        this.studentQuery = properties;
    }

    @Override
    public StudentEntity getStudentById(int id) {
            return jdbcTemplate.query(studentQuery.getProperty(GET_STUDENT_BY_ID), 
                    preparedStatement -> preparedStatement.setInt(1, id), 
                    resultSet -> {
                        StudentEntity student = new StudentEntity();
                        GroupEntity group = new GroupEntity();
                        
                        while(resultSet.next()) {
                            group.setId(resultSet.getInt(GROUP_ID));
                            student.setId(resultSet.getInt(ID));
                            student.setGroup(group);
                            student.setFirstName(resultSet.getString(FIRST_NAME));
                            student.setLastName(resultSet.getString(LAST_NAME));
                        }
                        
                        return student;
                    });
    }
}
