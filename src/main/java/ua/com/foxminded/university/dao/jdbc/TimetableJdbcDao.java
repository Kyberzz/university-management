package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TimetableMapper;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

@Slf4j
@Repository
public class TimetableJdbcDao implements TimetableDao {
    
    private static final String GET_COURCE_BY_TIMETABLE_ID = "timetable.getCourseByTimetableId";
    private static final String GET_GROUP_BY_TIMETABLE_ID = "timetable.getGroupByTimetableId";
    private static final String UPDATE = "timetable.update";
    private static final String DELETE_BY_ID = "timetable.deleteById";
    private static final String GET_BY_ID = "timetable.getById";
    private static final String INSERT = "timetable.insert";
    
    private JdbcTemplate jdbcTemplate;
    private Environment queries;
    private TimetableMapper timetableMapper;
    private CourseMapper courseMapper;
    private GroupMapper groupMapper;
    
    @Autowired
    public TimetableJdbcDao(JdbcTemplate jdbcTemplate, Environment queries, TimetableMapper timetableMapper,
            CourseMapper courseMapper, GroupMapper groupMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.timetableMapper = timetableMapper;
        this.courseMapper = courseMapper;
        this.groupMapper = groupMapper;
    }

    @Override
    public TimetableEntity getCourseByTimetableId(int id) throws DaoException {
        try {
            log.debug("Get course by timetable id={}.", id);
            String query = queries.getProperty(GET_COURCE_BY_TIMETABLE_ID);
            TimetableEntity timetableWithCourse = jdbcTemplate.queryForObject(
                    query, 
                    (resultSet, rowNum) -> {
                        TimetableEntity timetable = timetableMapper.mapRow(resultSet, rowNum);
                        CourseEntity course = courseMapper.mapRow(resultSet, rowNum);
                        timetable.setCourse(course);
                        return timetable;
                    }, 
                    id);
            log.trace("Course by timetable id={} was received.", timetableWithCourse.getId());
            return timetableWithCourse;
        } catch (DataAccessException e) {
            throw new DaoException("Getting the database timetable data failed.", e);
        }
    }
    
    @Override
    public TimetableEntity getGroupByTimetableId(int id) throws DaoException {
        try {
            log.debug("Get group by timetable id={}.", id);
            String query = queries.getProperty(GET_GROUP_BY_TIMETABLE_ID);
            TimetableEntity timetableWithGroup = jdbcTemplate.queryForObject(query, 
                    (resultSet, rowNum) -> {
                        TimetableEntity timetable = timetableMapper.mapRow(resultSet, rowNum);
                        GroupEntity group = groupMapper.mapRow(resultSet, rowNum);
                        timetable.setGroup(group);
                        return timetable;
                    }, 
                    id);
            log.trace("Group of timetable id={} was received.", timetableWithGroup.getId());
            return timetableWithGroup;
        } catch (DataAccessException e) {
            throw new DaoException("Getting the database grou data by the timetable id failed.", e);
        }
    }
    
    @Override
    public TimetableEntity getById(int id) throws DaoException {
        try {
            log.debug("Get timetable by id={}.", id);
            String query = queries.getProperty(GET_BY_ID);
            TimetableEntity timetable = jdbcTemplate.queryForObject(query, 
                    (resultSet, rowNum) -> timetableMapper.mapRow(resultSet, id), 
                    id);
            log.trace("Timetable with id={} was received.", timetable.getId());
            return timetable;
        } catch (DataAccessException e) {
            throw new DaoException("Getting the database timetable data by its id failed.", e);
        }
    }

    @Override
    public int update(TimetableEntity entity) throws DaoException {
        try {
            log.debug("Update timetable with id={}.", entity.getId());
            String query = queries.getProperty(UPDATE);
            
            int updatedTimetablesQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> {
                        preparedStatement.setObject(1, entity.getGroup().getId());
                        preparedStatement.setObject(2, entity.getCourse().getId());
                        preparedStatement.setLong(3, entity.getStartTime());
                        preparedStatement.setLong(4, entity.getEndTime());
                        preparedStatement.setString(5, entity.getDescription());
                        preparedStatement.setString(6, entity.getWeekDay().toString());
                        preparedStatement.setInt(7, entity.getId());
                    });
            log.trace("Timetable with id={} was updated.", entity.getId());
            return updatedTimetablesQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Updating the database timetable data failed.", e);
        }
    }
    
    @Override
    public int deleteById(int id) throws DaoException {
        try {
            log.debug("Delete timetable with id={}.", id);
            String query = queries.getProperty(DELETE_BY_ID);
            
            int deletedTimetablesQuantity = jdbcTemplate.update(query,
                    preparedStatement -> preparedStatement.setInt(1, id));
            log.trace("Timetable with id={} was deleted.", id);
            return deletedTimetablesQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Deleting the database timetable data by its id failed.", e);
        }
    }
    
    @Override
    public int insert(TimetableEntity entity) throws DaoException {
        try {
            log.debug("Insert timetable with id={}.", entity.getId());
            String query = queries.getProperty(INSERT);
            
            int insertedTimetablesQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> {
                        preparedStatement.setInt(1, entity.getGroup().getId());
                        preparedStatement.setInt(2, entity.getCourse().getId());
                        preparedStatement.setLong(3, entity.getStartTime());
                        preparedStatement.setLong(4, entity.getEndTime());
                        preparedStatement.setString(5, entity.getDescription());
                        preparedStatement.setString(6, entity.getWeekDay().toString());
                    });
            log.trace("Timetable with id={} was inserted to database.", entity.getId());
            return insertedTimetablesQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Inserting the timetable data to the database failed.", e);
        }
    }
}
