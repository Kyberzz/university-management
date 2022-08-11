package ua.com.foxminded.university.dao.jdbc;

import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

public class JdbcStudentDao implements StudentDao {
    
    private static final String GROUP_ID = "group_id";
    private static final String GET_STUDENT_BY_ID = "getStudentById";
    private static final String LAST_NAME = "last_name";
    private static final String FIRST_NAME = "first_name";
    private static final String STUDENT_ID = "id";
    
    private Properties studentQuery;
    private JdbcTemplate jdbcTemplate;
    
    public JdbcStudentDao(JdbcTemplate jdbcTemplate, Properties studentQuery) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentQuery = studentQuery;
    }
    
    @Override
    public StudentEntity getStudentById(int id) {
            return jdbcTemplate.query(studentQuery.getProperty(GET_STUDENT_BY_ID), 
                    preparedStatement -> preparedStatement.setInt(1, id), 
                    resultSet -> {
                        StudentEntity student = new StudentEntity();

                        while(resultSet.next()) {
                            student.setId(resultSet.getInt(STUDENT_ID));
                            student.setFirstName(resultSet.getString(FIRST_NAME));
                            student.setLastName(resultSet.getString(LAST_NAME));
                            
                            GroupEntity group = new GroupEntity();
                            group.setId(resultSet.getInt(GROUP_ID));
                            student.setGroup(group);
                        }
                        return student;
                    });
    }
}
