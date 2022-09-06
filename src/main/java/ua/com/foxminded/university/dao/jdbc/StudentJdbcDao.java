package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

@Repository
public class StudentJdbcDao implements StudentDao {
    
    private static final String DELETE_BY_ID = "student.deleteById";
    private static final String UPDATE = "student.update";
    private static final String GET_BY_ID = "student.getById";
    private static final String INSERT = "student.insert";
    private static final String GET_GROUP_BY_STUDENT_ID = "student.getGroupByStudentId";
    
    private JdbcTemplate jdbcTemplate;
    private Environment queries;
    private StudentMapper studentMapper;
    private GroupMapper groupMapper;
    
    public StudentJdbcDao(JdbcTemplate jdbcTemplate, Environment queries, StudentMapper studentMapper,
            GroupMapper groupMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.studentMapper = studentMapper;
        this.groupMapper = groupMapper;
    }

    @Override
    public StudentEntity getGroupByStudentId(int id) {
        String sqlGetGroupByStudentId = queries.getProperty(GET_GROUP_BY_STUDENT_ID);
        StudentEntity studentWithGroupData = jdbcTemplate.queryForObject(
                sqlGetGroupByStudentId, 
                (resultSet, rowNum) -> {
                    StudentEntity student = studentMapper.mapRow(resultSet, rowNum);
                    GroupEntity group = groupMapper.mapRow(resultSet, rowNum);
                    student.setGroup(group);
                    return student;
                }, 
                id);
        return studentWithGroupData;
    }
    
    @Override
    public int insert(StudentEntity entity) {
        String sqlInsertStudent = queries.getProperty(INSERT);
        return jdbcTemplate.update(sqlInsertStudent, 
                                   preparedStatement -> getPreparedStatementOfInsert(preparedStatement, 
                                                                                     entity));
    }
    
    @Override
    public StudentEntity getById(int id) {
        String sqlGetStudentById = queries.getProperty(GET_BY_ID);
        StudentEntity student = jdbcTemplate.queryForObject(sqlGetStudentById, 
        (resultSet, rowNum) -> studentMapper.mapRow(resultSet, rowNum), 
        id);
        return student;
    }
    
    @Override
    public int update(StudentEntity entity) {
        String sqlUdateStudent = queries.getProperty(UPDATE);
        return jdbcTemplate.update(sqlUdateStudent, 
                                   preparedStatement -> getPreparedStetamentOfUdate(preparedStatement, 
                                                                                    entity));
    }
    
    public int deleteById(int id) {
        String sqlDeleteStudentById = queries.getProperty(DELETE_BY_ID);
        return jdbcTemplate.update(sqlDeleteStudentById, 
                                   preparedStatement -> preparedStatement.setInt(1, id));
        
    }
    
    private PreparedStatement getPreparedStatementOfInsert(PreparedStatement preparedStatement, 
                                                           StudentEntity entity) throws SQLException {
        preparedStatement.setString(1, entity.getFirstName());
        preparedStatement.setString(2, entity.getLastName());
        preparedStatement.setInt(3, entity.getGroup().getId());
        return preparedStatement;
    }
    
    private PreparedStatement getPreparedStetamentOfUdate(PreparedStatement preparedStatement, 
                                                          StudentEntity entity) throws SQLException {
        preparedStatement.setString(1, entity.getFirstName());
        preparedStatement.setString(2, entity.getLastName());
        preparedStatement.setObject(3, entity.getGroup().getId());
        preparedStatement.setInt(4, entity.getId());
        return preparedStatement;
    }
}
