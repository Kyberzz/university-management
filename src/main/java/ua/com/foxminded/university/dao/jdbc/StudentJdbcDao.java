package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

@Repository
public class StudentJdbcDao implements StudentDao {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentJdbcDao.class);
    private static final String DELETE_BY_ID_ERROR = "Deleting the database student data failed.";
    private static final String UPDATE_ERROR = "Updating the database studen data failed.";
    private static final String GET_BY_ID_ERROR = "Getting the database student data by its id failed.";
    private static final String SETTING_PREPARED_STATEMENT_ERROR = "Setting the prepared statement failed.";
    private static final String INSERT_ERROR = "Inserting the student data to the database failed.";
    private static final String GET_GROUP_BY_STUDENT_ID_ERROR = "Getting the student data "
            + "by its id from the database failed.";
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
    public StudentEntity getGroupByStudentId(int id) throws DaoException {
        try {
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
        } catch (DataAccessException e) {
            LOGGER.error(GET_GROUP_BY_STUDENT_ID_ERROR, e);
            throw new DaoException(GET_GROUP_BY_STUDENT_ID_ERROR, e);
        }
    }
    
    @Override
    public int insert(StudentEntity entity) throws DaoException {
        try {
            String sqlInsertStudent = queries.getProperty(INSERT);
            return jdbcTemplate.update(sqlInsertStudent, 
                                       preparedStatement -> {
                                        try {
                                                getPreparedStatementOfInsert(preparedStatement, entity);
                                            } catch (DaoException e) {
                                                LOGGER.error(INSERT_ERROR);
                                            }
                                       });
        } catch (DataAccessException e) {
            LOGGER.error(INSERT_ERROR, e);
            throw new DaoException(INSERT_ERROR, e);
        }
    }
    
    @Override
    public StudentEntity getById(int id) throws DaoException {
        try {
            String sqlGetStudentById = queries.getProperty(GET_BY_ID);
            StudentEntity student = jdbcTemplate.queryForObject(sqlGetStudentById, 
            (resultSet, rowNum) -> studentMapper.mapRow(resultSet, rowNum), 
            id);
            return student;
        } catch (DataAccessException e) {
            LOGGER.error(GET_BY_ID_ERROR, e);
            throw new DaoException(GET_BY_ID_ERROR, e);
        }
    }
    
    @Override
    public int update(StudentEntity entity) throws DaoException {
        try {
            String sqlUdateStudent = queries.getProperty(UPDATE);
            return jdbcTemplate.update(sqlUdateStudent, 
                                       preparedStatement -> {
                                        try {
                                            getPreparedStetamentOfUdate(preparedStatement, entity);
                                        } catch (DaoException e) {
                                            LOGGER.error(UPDATE_ERROR);
                                        }
                                    });
        } catch (DataAccessException e) {
            LOGGER.error(UPDATE_ERROR, e);
            throw new DaoException(UPDATE_ERROR, e);
        }
    }
    
    public int deleteById(int id) throws DaoException {
        try {
            String sqlDeleteStudentById = queries.getProperty(DELETE_BY_ID);
            return jdbcTemplate.update(sqlDeleteStudentById, 
                                       preparedStatement -> preparedStatement.setInt(1, id));
        } catch (DataAccessException e) {
            LOGGER.error(DELETE_BY_ID_ERROR, e);
            throw new DaoException(DELETE_BY_ID_ERROR, e);
        }
    }
    
    private PreparedStatement getPreparedStatementOfInsert(PreparedStatement preparedStatement, 
                                                           StudentEntity entity) throws DaoException {
        try {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setInt(3, entity.getGroup().getId());
            return preparedStatement;
        } catch (SQLException e) {
            LOGGER.error(SETTING_PREPARED_STATEMENT_ERROR, e);
            throw new DaoException(SETTING_PREPARED_STATEMENT_ERROR, e);
        }
    }
    
    private PreparedStatement getPreparedStetamentOfUdate(PreparedStatement preparedStatement, 
                                                          StudentEntity entity) throws DaoException {
        try {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setObject(3, entity.getGroup().getId());
            preparedStatement.setInt(4, entity.getId());
            return preparedStatement;
        } catch (SQLException e) {
            LOGGER.error(SETTING_PREPARED_STATEMENT_ERROR, e);
            throw new DaoException(SETTING_PREPARED_STATEMENT_ERROR, e);
        }
    }
}
