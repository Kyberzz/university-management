package ua.com.foxminded.university.dao.jdbc;

import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

public class JdbcStudentDao implements StudentDao {
    
    private static final String GROUP_NAME = "group_name";
    private static final String GET_GROUP_BY_STUDENT_ID = "getGroupByStudentId";
    private static final String INSERT = "insert";
    private static final String GET_BY_ID = "getById";
    private static final String UPDATE = "update";
    private static final String DELETE_BY_ID = "deleteById";
    private static final String GROUP_ID = "group_id";
    private static final String LAST_NAME = "last_name";
    private static final String FIRST_NAME = "first_name";
    private static final String STUDENT_ID = "id";
    
    private Properties studentQueries;
    private JdbcTemplate jdbcTemplate;
    
    public JdbcStudentDao(JdbcTemplate jdbcTemplate, Properties studentQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentQueries = studentQueries;
    }
    
    public StudentEntity getGroupByStudentId(int id) {
        return jdbcTemplate.query(studentQueries.getProperty(GET_GROUP_BY_STUDENT_ID),
                                  preparedStatement -> preparedStatement.setInt(1, id),
                                  resultSet -> {
                                      StudentEntity student = new StudentEntity();
                                      GroupEntity group = new GroupEntity();
                                      
                                      while (resultSet.next()) {
                                          group.setId(resultSet.getInt(GROUP_ID));
                                          group.setName(resultSet.getString(GROUP_NAME));
                                          student.setGroup(group);
                                          student.setId(resultSet.getInt(STUDENT_ID));
                                          student.setFirstName(resultSet.getString(FIRST_NAME));
                                          student.setLastName(resultSet.getString(LAST_NAME));
                                      }
                                      return student;
                                  });
    }
    
    public int insert(StudentEntity entity) {
        return jdbcTemplate.update(studentQueries.getProperty(INSERT), 
                                   preparedStatement -> {
                                       preparedStatement.setString(1, entity.getFirstName());
                                       preparedStatement.setString(2, entity.getLastName());
                                       preparedStatement.setInt(3, entity.getGroup().getId());
                                   });
    }
    
    public StudentEntity getById(int id) {
        return jdbcTemplate.query(studentQueries.getProperty(GET_BY_ID), 
                                  preparedStatement -> preparedStatement.setInt(1, id), 
                                  resultSet -> {
                                      StudentEntity student = new StudentEntity();
                                      
                                      while (resultSet.next()) {
                                          student.setId(resultSet.getInt(STUDENT_ID));
                                          student.setFirstName(resultSet.getString(FIRST_NAME));
                                          student.setLastName(resultSet.getString(LAST_NAME));
                                          student.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
                                      }
                                      return student;
                                  });
    }
    
    public int update(StudentEntity entity) {
        return jdbcTemplate.update(studentQueries.getProperty(UPDATE), 
                                   preparedStatement -> {
                                       preparedStatement.setString(1, entity.getFirstName());
                                       preparedStatement.setString(2, entity.getLastName());
                                       preparedStatement.setInt(3, entity.getGroup().getId());
                                       preparedStatement.setInt(4, entity.getId());
                                   });
    }
    
    public int deleteById(int id) {
        return jdbcTemplate.update(studentQueries.getProperty(DELETE_BY_ID), 
                                   preparedStatement -> preparedStatement.setInt(1, id));
        
    }
}
