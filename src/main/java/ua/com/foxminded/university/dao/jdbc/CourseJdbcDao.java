package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TimetableMapper;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

@Repository
public class CourseJdbcDao implements CourseDao {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseJdbcDao.class);
    private static final String GET_TIMETABLE_LIST_BY_COURSE_ID = "course.getTimetableListByCourseId";
    private static final String UPDATE = "course.update";
    private static final String GET_BY_ID = "course.getById";
    private static final String INSERT = "course.insert";
    private static final String DELETE_BY_ID = "course.deleteById";
    
    private JdbcTemplate jdbcTemplate;
    private Environment queries;
    private CourseMapper courseMapper;
    private TimetableMapper timetableMapper;
    
    @Autowired
    public CourseJdbcDao(JdbcTemplate jdbcTemplate, Environment queries, CourseMapper courseMapper,
            TimetableMapper timetableMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.courseMapper = courseMapper;
        this.timetableMapper = timetableMapper;
    }

    @Override
    public CourseEntity getTimetableListByCourseId(int id) throws DaoException {
        String errorMessage = "Getting the timetable list by course id failed.";
        
        try {
            String sqlGetTimetableListByCourseId = queries.getProperty(GET_TIMETABLE_LIST_BY_COURSE_ID);
            CourseEntity courseWhithTimetableList = jdbcTemplate.queryForObject(
                    sqlGetTimetableListByCourseId,                                   
                    (resultSet, rowNum) -> {
                        CourseEntity course = null;
                        if (course == null) {
                            course = courseMapper.mapRow(resultSet, rowNum);
                            course.setTimetableList(new ArrayList<>());
                        }
                        
                        TimetableEntity timetable = timetableMapper.mapRow(resultSet, id);
                        course.getTimetableList().add(timetable);
                        return course;
                    },
                    id);
            return courseWhithTimetableList;
        } catch (DataAccessException e) {
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int deleteById(int id) throws DaoException {
        String errorMessage = "Deleting the course by its id failed.";
        
        try {
            String sqlDeleteCourseById = queries.getProperty(DELETE_BY_ID);
            return jdbcTemplate.update(sqlDeleteCourseById,
                                       preparedStatement -> preparedStatement.setInt(1,id));
        } catch (DataAccessException e) {
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int update(CourseEntity entity) throws DaoException {
        String errorMessage = "Updating the course data failed.";
        
        try {
            String sqlUpdateCourse = queries.getProperty(UPDATE);
            return jdbcTemplate.update(sqlUpdateCourse,
                    preparedStatement -> {
                        preparedStatement.setObject(1, entity.getTeacher().getId()); 
                        preparedStatement.setString(2, entity.getName());
                        preparedStatement.setString(3, entity.getDescription());
                        preparedStatement.setInt(4, entity.getId());
                    });
        } catch (DataAccessException e) {
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public CourseEntity getById(int id) throws DaoException {
        String errorMessage = "Getting the database course data by its id failed.";
        
        try {
            String sqlGetCourseById = queries.getProperty(GET_BY_ID);
            CourseEntity courseEntity = jdbcTemplate.queryForObject(sqlGetCourseById, 
                    (resultSet, rowNum) -> courseMapper.mapRow(resultSet, rowNum), 
                    id);
            return courseEntity;
        } catch (DataAccessException e) {
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int insert(CourseEntity entity) throws DaoException {
        String errorMessage = "Inserting the course to the database failed.";
        
        try {
            String sqlInsertCourse = queries.getProperty(INSERT);
            return jdbcTemplate.update(sqlInsertCourse,
                                       preparedStatement -> {
                                           preparedStatement.setObject(1, entity.getTeacher().getId()); 
                                           preparedStatement.setString(2, entity.getName());
                                           preparedStatement.setString(3, entity.getDescription());
                                       });
        } catch (DataAccessException e) {
            logger.error(errorMessage);
            throw new DaoException(errorMessage, e);
        }
    }
}
