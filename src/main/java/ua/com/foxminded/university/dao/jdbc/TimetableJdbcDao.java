package ua.com.foxminded.university.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TimetableMapper;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

@Repository
public class TimetableJdbcDao implements TimetableDao {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TimetableJdbcDao.class);
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
            String sqlGetCourseByTimetalbeId = queries.getProperty(GET_COURCE_BY_TIMETABLE_ID);
            TimetableEntity timetableWithCourseList = jdbcTemplate.queryForObject(
                    sqlGetCourseByTimetalbeId, 
                    (resultSet, rowNum) -> {
                        TimetableEntity timetable = timetableMapper.mapRow(resultSet, rowNum);
                        CourseEntity course = courseMapper.mapRow(resultSet, rowNum);
                        timetable.setCourse(course);
                        return timetable;
                    }, 
                    id);
            return timetableWithCourseList;
        } catch (DataAccessException e) {
            String errorMessage = "Getting the database "
                    + "timetable data failed.";
            LOGGER.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
        
    }
    
    @Override
    public TimetableEntity getGroupByTimetableId(int id) throws DaoException {
        try {
            String sqlGetGroupByTimetableId = queries.getProperty(GET_GROUP_BY_TIMETABLE_ID);
            TimetableEntity timetableWithGroupList = jdbcTemplate.queryForObject(
                    sqlGetGroupByTimetableId, 
                    (resultSet, rowNum) -> {
                        TimetableEntity timetable = timetableMapper.mapRow(resultSet, rowNum);
                        GroupEntity group = groupMapper.mapRow(resultSet, rowNum);
                        timetable.setGroup(group);
                        return timetable;
                    }, 
                    id);
            return timetableWithGroupList;
        } catch (DataAccessException e) {
            String errorMessage = "Getting the database grou data by the timetable id failed.";
            LOGGER.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
        
    }
    
    @Override
    public TimetableEntity getById(int id) throws DaoException {
        try {
            String slqGetTimetableById = queries.getProperty(GET_BY_ID);
            TimetableEntity timetable = jdbcTemplate.queryForObject(slqGetTimetableById, 
                    (resultSet, rowNum) -> timetableMapper.mapRow(resultSet, id), 
                    id);
            return timetable;
        } catch (DataAccessException e) {
            String errorMessage = "Getting the database timetable data by its id failed.";
            LOGGER.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }

    @Override
    public int update(TimetableEntity entity) throws DaoException {
        try {
            String sqlUpdateTimetable = queries.getProperty(UPDATE);
            return jdbcTemplate.update(sqlUpdateTimetable,
                                       preparedStatement -> {
                                           preparedStatement.setObject(1, entity.getGroup().getId());
                                           preparedStatement.setObject(2, entity.getCourse().getId());
                                           preparedStatement.setLong(3, entity.getStartTime());
                                           preparedStatement.setLong(4, entity.getEndTime());
                                           preparedStatement.setString(5, entity.getDescription());
                                           preparedStatement.setString(6, entity.getWeekDay().toString());
                                           preparedStatement.setInt(7, entity.getId());
                                       });
        } catch (DataAccessException e) {
            String errorMessage = "Updating the database timetable data failed.";
            LOGGER.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
      
    }
    
    @Override
    public int deleteById(int id) throws DaoException {
        try {
            String slqDeleteTimetableById = queries.getProperty(DELETE_BY_ID);
            return jdbcTemplate.update(slqDeleteTimetableById,
                                       preparedStatement -> preparedStatement.setInt(1, id));
        } catch (DataAccessException e) {
            String errorMessage = "Deleting the database timetable data by its id failed.";
            LOGGER.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int insert(TimetableEntity entity) throws DaoException {
        try {
            String sqlInsertTimetable = queries.getProperty(INSERT);
            return jdbcTemplate.update(sqlInsertTimetable,
                                       preparedStatement -> {
                                           preparedStatement.setInt(1, entity.getGroup().getId());
                                           preparedStatement.setInt(2, entity.getCourse().getId());
                                           preparedStatement.setLong(3, entity.getStartTime());
                                           preparedStatement.setLong(4, entity.getEndTime());
                                           preparedStatement.setString(5, entity.getDescription());
                                           preparedStatement.setString(6, entity.getWeekDay().toString());
                                       });
        } catch (DataAccessException e) {
            String errorMessage = "Inserting the timetable data to the database failed.";
            LOGGER.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
}
