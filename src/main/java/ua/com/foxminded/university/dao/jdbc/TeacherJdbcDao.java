package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TeacherMapper;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;

@Repository
public class TeacherJdbcDao implements TeacherDao {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherJdbcDao.class);
    private static final String INSERT_ERROR = "Inserting the database teacher data failed.";
    private static final String GET_BY_ID_ERROR = "Getting the database teacher data failed.";
    private static final String GET_PREPARED_STATEMENT_ERROR = "Setting the prepared statement failed.";
    private static final String UPDATE_ERROR = "Updating the database teacher data failed.";
    private static final String DELETE_BY_ID_ERROR = "Deleting the database teacher data failed.";
    private static final String GET_COURSE_LIST_BY_TEACHER_ID_ERROR = "Getting the course list data "
            + "by the teacher id from the database failed.";
    private static final String GET_COURSE_LIST_BY_TEACHER_ID = "teacher.getCourseListByTeacherId";
    private static final String DELETE_BY_ID = "teacher.deleteById";
    private static final String UPDATE = "teacher.update";
    private static final String INSERT = "teacher.insert";
    private static final String GET_BY_ID = "teacher.getById";
    
    private JdbcTemplate jdbcTemplate;
    private Environment queries;
    private TeacherMapper teacherMapper;
    private CourseMapper courseMapper;

    public TeacherJdbcDao(JdbcTemplate jdbcTemplate, Environment queries, TeacherMapper teacherMapper,
            CourseMapper courseMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.teacherMapper = teacherMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    public TeacherEntity getCourseListByTeacherId(int id) throws DaoException {
        try {
            String sqlGetCourseListByTeacherId = queries.getProperty(GET_COURSE_LIST_BY_TEACHER_ID);
            TeacherEntity teacherWithCourseList = jdbcTemplate.queryForObject(
                    sqlGetCourseListByTeacherId, 
                    (resultSet, rowNum) -> {
                        TeacherEntity teacher = null;
                        
                        if (teacher == null) {
                            teacher = teacherMapper.mapRow(resultSet, rowNum);
                            teacher.setCourseList(new ArrayList<>());
                        }

                        CourseEntity course = courseMapper.mapRow(resultSet, rowNum);
                        teacher.getCourseList().add(course);
                        return teacher;
                    }, 
                    id);
            return teacherWithCourseList;
        } catch (DataAccessException e) {
            LOGGER.error(GET_COURSE_LIST_BY_TEACHER_ID_ERROR, e);
            throw new DaoException(GET_COURSE_LIST_BY_TEACHER_ID_ERROR, e);
        }
    }
    
    @Override
    public int deleteById(int id) throws DaoException {
        try {
            String sqlDeleteTeacherByid = queries.getProperty(DELETE_BY_ID);
            return jdbcTemplate.update(sqlDeleteTeacherByid, 
                                       preparedStatement -> preparedStatement.setInt(1, id));
        } catch (DataAccessException e) {
            LOGGER.error(DELETE_BY_ID_ERROR, e);
            throw new DaoException(DELETE_BY_ID_ERROR, e);
        }
    }
    
    @Override
    public int update(TeacherEntity entity) throws DaoException {
        try {
            String sqlUpdateTeacher = queries.getProperty(UPDATE);
            return jdbcTemplate.update(sqlUpdateTeacher, 
                                       preparedStatement -> {
                                        try {
                                            getPreparedStatementOfUpdate(preparedStatement, entity);
                                        } catch (DaoException e) {
                                            LOGGER.error(UPDATE_ERROR, e);
                                        }
                                    });
            
        } catch (DataAccessException e){
            LOGGER.error(UPDATE_ERROR, e);
            throw new DaoException(UPDATE_ERROR, e);
        }
    }
    
    @Override
    public TeacherEntity getById(int id) throws DaoException {
        try {
            String sqlGetTeacherById = queries.getProperty(GET_BY_ID);
            TeacherEntity teacherEntity = jdbcTemplate.queryForObject(sqlGetTeacherById,
                    (resultSet, rowNum) -> teacherMapper.mapRow(resultSet, rowNum),
                    id);
            return teacherEntity;
        } catch (DataAccessException e) {
            LOGGER.error(GET_BY_ID_ERROR, e);
            throw new DaoException(GET_BY_ID_ERROR, e);
        }
    }
    
    @Override
    public int insert(TeacherEntity entity) throws DaoException {
        try {
            String sqlInsertTeacher = queries.getProperty(INSERT);
            return jdbcTemplate.update(sqlInsertTeacher, 
                                       preparedStatement -> {
                                           preparedStatement.setString(1, entity.getFirstName());
                                           preparedStatement.setString(2, entity.getLastName());
                                       });
        } catch (DataAccessException e) {
            LOGGER.error(INSERT_ERROR, e);
            throw new DaoException(INSERT_ERROR, e);
        }
    }
    
    private PreparedStatement getPreparedStatementOfUpdate(PreparedStatement preparedStatement, 
                                                           TeacherEntity entity) throws DaoException {
        try {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setInt(3, entity.getId());
            return preparedStatement;
        } catch (SQLException e) {
            LOGGER.error(GET_PREPARED_STATEMENT_ERROR, e);
            throw new DaoException(GET_PREPARED_STATEMENT_ERROR, e);
        }
    }
}
