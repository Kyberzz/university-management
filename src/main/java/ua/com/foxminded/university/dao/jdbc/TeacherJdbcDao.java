package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    private static final Logger logger = LoggerFactory.getLogger(TeacherJdbcDao.class);
    private static final String GET_COURSE_LIST_BY_TEACHER_ID = "teacher.getCourseListByTeacherId";
    private static final String DELETE_BY_ID = "teacher.deleteById";
    private static final String UPDATE = "teacher.update";
    private static final String INSERT = "teacher.insert";
    private static final String GET_BY_ID = "teacher.getById";
    
    private JdbcTemplate jdbcTemplate;
    private Environment queries;
    private TeacherMapper teacherMapper;
    private CourseMapper courseMapper;
    
    @Autowired
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
            logger.debug("Get courses list by teacher id={}.", id);
            String query = queries.getProperty(GET_COURSE_LIST_BY_TEACHER_ID);
            TeacherEntity teacherWithCourseList = jdbcTemplate.query(query, 
                    preparedStatement -> preparedStatement.setInt(1, id), 
                    (resultSet) -> {
                        TeacherEntity teacher = null;
                        
                        while (resultSet.next()) {
                            if (teacher == null) {
                                teacher = teacherMapper.mapRow(resultSet, resultSet.getRow());
                                teacher.setCourseList(new ArrayList<>());
                            }

                            CourseEntity course = courseMapper.mapRow(resultSet, resultSet.getRow());
                            teacher.getCourseList().add(course);
                        }
                        return teacher;
                    });
            logger.trace("Courses list of teacher id={} was received");
            return teacherWithCourseList;
        } catch (DataAccessException e) {
            String errorMessage = "Getting the course list data by the teacher id from the database failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int deleteById(int id) throws DaoException {
        try {
            logger.debug("Delete teacher with id={}.", id);
            String query = queries.getProperty(DELETE_BY_ID);
            int deletedTeachersQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> preparedStatement.setInt(1, id));
            logger.trace("Teacher with id={} was deleted.", id);
            return deletedTeachersQuantity;
        } catch (DataAccessException e) {
            String errorMessage = "Deleting the database teacher data failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int update(TeacherEntity entity) throws DaoException {
        try {
            logger.debug("Udate teacher with id={}.", entity.getId());
            String query = queries.getProperty(UPDATE);
            int updatedTeachersQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> {
                        preparedStatement.setString(1, entity.getFirstName());
                        preparedStatement.setString(2, entity.getLastName());
                        preparedStatement.setInt(3, entity.getId());
                    });
            logger.trace("Teacher with id={} was updated.", entity.getId());
            return updatedTeachersQuantity;
        } catch (DataAccessException e){
            String errorMessage = "Updating the database teacher data failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public TeacherEntity getById(int id) throws DaoException {
        try {
            logger.debug("Get teacher with id={}.", id);
            String query = queries.getProperty(GET_BY_ID);
            TeacherEntity teacherEntity = jdbcTemplate.queryForObject(query,
                    (resultSet, rowNum) -> teacherMapper.mapRow(resultSet, rowNum),
                    id);
            logger.trace("Teacher with id={} was received.", teacherEntity.getId());
            return teacherEntity;
        } catch (DataAccessException e) {
            String errorMessage = "Getting the database teacher data failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int insert(TeacherEntity entity) throws DaoException {
        try {
            logger.debug("Insert teacher with id={} to the database.", entity.getId());
            String query = queries.getProperty(INSERT);
            int insertedTeachersQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> {
                        preparedStatement.setString(1, entity.getFirstName());
                        preparedStatement.setString(2, entity.getLastName());
                    });
            logger.trace("Teacher with id={} was added to the database.", entity.getId());
            return insertedTeachersQuantity;
        } catch (DataAccessException e) {
            String errorMessage = "Inserting the database teacher data failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
}
