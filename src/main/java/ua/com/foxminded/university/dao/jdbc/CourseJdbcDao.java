package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TimetableMapper;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

@Slf4j
@Repository
public class CourseJdbcDao implements CourseDao {
    
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
        try {
            log.debug("Get timetable list by course id={}", id);
            String query = queries.getProperty(GET_TIMETABLE_LIST_BY_COURSE_ID);
            CourseEntity courseWhithTimetableList = jdbcTemplate.query(query, 
                    preparedStatement -> preparedStatement.setInt(1, id), 
                    (resultSet) -> {
                        CourseEntity course = null;
                        
                        while(resultSet.next()) {
                            if (course == null) {
                                course = courseMapper.mapRow(resultSet, resultSet.getRow());
                                course.setTimetableList(new ArrayList<>());
                            }
                            
                            TimetableEntity timetable = timetableMapper.mapRow(resultSet, id);
                            course.getTimetableList().add(timetable);
                        }
                        return course;
                    });
            log.trace("Timetable list of course with id={} was received.", courseWhithTimetableList.getId());
            return courseWhithTimetableList;
        } catch (DataAccessException e) {
            throw new DaoException("Getting the timetable list by course id failed.", e);
        }
    }
    
    @Override
    public int deleteById(int id) throws DaoException {
        try {
            log.debug("Delete course by id={}", id);
            String query = queries.getProperty(DELETE_BY_ID);
            
            int deletedCoursesQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> preparedStatement.setInt(1,id));
            log.trace("Course with id={} was deleted.", id);
            return deletedCoursesQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Deleting the course by its id failed.", e);
        }
    }
    
    @Override
    public int update(CourseEntity entity) throws DaoException {
        try {
            log.debug("Update course with id={}", entity.getId());
            String query = queries.getProperty(UPDATE);
            
            int updatedCoursesQuantity = jdbcTemplate.update(query,
                    preparedStatement -> {
                        preparedStatement.setObject(1, entity.getTeacher().getId()); 
                        preparedStatement.setString(2, entity.getName());
                        preparedStatement.setString(3, entity.getDescription());
                        preparedStatement.setInt(4, entity.getId());
                    });
            log.trace("Course with id ={} was updated.", entity.getId());
            return updatedCoursesQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Updating the course data failed.", e);
        }
    }
    
    @Override
    public CourseEntity getById(int id) throws DaoException {
        try {
            log.debug("Get course by id={}", id);
            String query = queries.getProperty(GET_BY_ID);
            CourseEntity courseEntity = jdbcTemplate.queryForObject(query, 
                    (resultSet, rowNum) -> courseMapper.mapRow(resultSet, rowNum), 
                    id);
            log.trace("Course with id={} was received.", courseEntity.getId());
            return courseEntity;
        } catch (DataAccessException e) {
            throw new DaoException("Getting the course by its id failed.", e);
        }
    }
    
    @Override
    public int insert(CourseEntity entity) throws DaoException {
        try {
            log.debug("Insert course with id={}", entity.getId());
            String query = queries.getProperty(INSERT);
            
            int insertedCoursesQuantity = jdbcTemplate.update(query,
                    preparedStatement -> {
                        preparedStatement.setObject(1, entity.getTeacher().getId()); 
                        preparedStatement.setString(2, entity.getName());
                        preparedStatement.setString(3, entity.getDescription());
                    });
            log.trace("Course with id={} was inserted.", entity.getId());
            return insertedCoursesQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Inserting the course to the database failed.", e);
        }
    }
}
