package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TimetableMapper;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

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
    public TimetableEntity getCourseByTimetableId(int id) {
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
    }
    
    @Override
    public TimetableEntity getGroupByTimetableId(int id) {
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
    }
    
    @Override
    public TimetableEntity getById(int id) {
        String slqGetTimetableById = queries.getProperty(GET_BY_ID);
        TimetableEntity timetable = jdbcTemplate.queryForObject(slqGetTimetableById, 
                (resultSet, rowNum) -> timetableMapper.mapRow(resultSet, id), 
                id);
        return timetable;
    }

    @Override
    public int update(TimetableEntity entity) {
        String sqlUpdateTimetable = queries.getProperty(UPDATE);
        return jdbcTemplate.update(sqlUpdateTimetable,
                                   preparedStatement -> getPreparedStatementOfUpdate(preparedStatement, 
                                                                                     entity));
    }
    
    @Override
    public int deleteById(int id) {
        String slqDeleteTimetableById = queries.getProperty(DELETE_BY_ID);
        return jdbcTemplate.update(slqDeleteTimetableById,
                                   preparedStatement -> preparedStatement.setInt(1, id));
    }
    
    @Override
    public int insert(TimetableEntity entity) {
        String sqlInsertTimetable = queries.getProperty(INSERT);
        return jdbcTemplate.update(sqlInsertTimetable,
                                   preparedStatement -> getPreparedStatementOfInsert(preparedStatement, 
                                                                                     entity));
    }
    
    private PreparedStatement getPreparedStatementOfUpdate(PreparedStatement preparedStatement, 
                                                           TimetableEntity entity) throws SQLException {
        preparedStatement.setObject(1, entity.getGroup().getId());
        preparedStatement.setObject(2, entity.getCourse().getId());
        preparedStatement.setLong(3, entity.getStartTime());
        preparedStatement.setLong(4, entity.getEndTime());
        preparedStatement.setString(5, entity.getDescription());
        preparedStatement.setString(6, entity.getWeekDay().toString());
        preparedStatement.setInt(7, entity.getId());
        return preparedStatement;
    }
    
    private PreparedStatement getPreparedStatementOfInsert(PreparedStatement preparedStatement, 
                                                           TimetableEntity entity) throws SQLException {
        preparedStatement.setInt(1, entity.getGroup().getId());
        preparedStatement.setInt(2, entity.getCourse().getId());
        preparedStatement.setLong(3, entity.getStartTime());
        preparedStatement.setLong(4, entity.getEndTime());
        preparedStatement.setString(5, entity.getDescription());
        preparedStatement.setString(6, entity.getWeekDay().toString());
        return preparedStatement;
    }
}
