package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;

@Repository
public class TimetableJdbcDao implements TimetableDao {
    
    private static final String GET_COURCE_BY_TIMETABLE_ID = "timetable.getCourseByTimetableId";
    private static final String GET_GROUP_BY_TIMETABLE_ID = "timetable.getGroupByTimetableId";
    private static final String UPDATE = "timetable.update";
    private static final String DELETE_BY_ID = "timetable.deleteById";
    private static final String GET_BY_ID = "timetable.getById";
    private static final String INSERT = "timetable.insert";
    private static final String GROUP_NAME = "group_name";
    private static final String WEEK_DAY_COLUMN = "week_day";
    private static final String TIMETABLE_DESCRIPTION = "description";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String GROUP_ID = "group_id";
    private static final String TEACHER_ID = "teacher_id";
    private static final String COURSE_DESCRIPTION = "course_description";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_ID = "course_id";
    private static final String TIMETABLE_ID = "id";
    
    private JdbcTemplate jdbcTemplate;
    private Environment timetableQueries;
    
    @Autowired
    public TimetableJdbcDao(JdbcTemplate jdbcTemplate, Environment timetableQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.timetableQueries = timetableQueries;
    }
    
    
    @Override
    public TimetableEntity getCourseByTimetableId(int id) {
        TimetableEntity timetableWithCourseData = jdbcTemplate.query(
                timetableQueries.getProperty(GET_COURCE_BY_TIMETABLE_ID),
                preparedStatement -> preparedStatement.setInt(1, id), 
                resultSet -> {
                    return getTimetableWithCourseData(resultSet);
                });
        return timetableWithCourseData;
    }
    
    @Override
    public TimetableEntity getGroupByTimetableId(int id) {
        TimetableEntity timetableWithGroupData = jdbcTemplate.query(
                timetableQueries.getProperty(GET_GROUP_BY_TIMETABLE_ID),
                preparedStatement -> preparedStatement.setInt(1, id), 
                resultSet -> {
                    return getTimetableWithGroupData(resultSet);
                });
        return timetableWithGroupData;
    }
    
    @Override
    public TimetableEntity getById(int id) {
        TimetableEntity timetableEntity = jdbcTemplate.query(
                timetableQueries.getProperty(GET_BY_ID),
                preparedStatament -> preparedStatament.setInt(1, id), 
                resultSet -> {
                    TimetableEntity timetable = null;

                    while (resultSet.next()) {
                        timetable = getTimetableEntity(resultSet);

                    }
                    return timetable;
                });
        return timetableEntity;
    }

    @Override
    public int update(TimetableEntity entity) {
        return jdbcTemplate.update(timetableQueries.getProperty(UPDATE),
                                   preparedStatement -> getPreparedStatementOfUpdate(preparedStatement, 
                                                                                     entity));
    }
    
    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(timetableQueries.getProperty(DELETE_BY_ID),
                                   preparedStatement -> preparedStatement.setInt(1, id));
    }
    
    @Override
    public int insert(TimetableEntity entity) {
        return jdbcTemplate.update(timetableQueries.getProperty(INSERT),
                                   preparedStatement -> getPreparedStatementOfInsert(preparedStatement, 
                                                                                     entity));
    }
    
    private TimetableEntity getTimetableWithCourseData(ResultSet resultSet) throws SQLException {
        TimetableEntity timetable = null;

        while (resultSet.next()) {
            CourseEntity course = getCourseEntity(resultSet);
            timetable = getTimetableEntity(resultSet);
            timetable.setCourse(course);
        }
        return timetable;
    }
    
    private TimetableEntity getTimetableWithGroupData(ResultSet resultSet) throws SQLException {
        TimetableEntity timetable = null;

        while (resultSet.next()) {
            timetable = getTimetableEntity(resultSet);
            GroupEntity group = new GroupEntity();
            group.setId(resultSet.getInt(GROUP_ID));
            group.setName(resultSet.getString(GROUP_NAME));
            timetable.setGroup(group);
        }
        return timetable;
    }
    
    private CourseEntity getCourseEntity(ResultSet resultSet) throws SQLException {
        CourseEntity course = new CourseEntity();
        course.setId(resultSet.getInt(COURSE_ID));
        course.setName(resultSet.getString(COURSE_NAME));
        course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
        course.setTeacher(new TeacherEntity(resultSet.getInt(TEACHER_ID)));
        return course;
    }
    
    private TimetableEntity getTimetableEntity(ResultSet resultSet) throws SQLException {
        TimetableEntity timetable = new TimetableEntity();
        timetable.setId(resultSet.getInt(TIMETABLE_ID));
        timetable.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
        timetable.setCourse(new CourseEntity(resultSet.getInt(COURSE_ID)));
        timetable.setStartTime(resultSet.getLong(START_TIME));
        timetable.setEndTime(resultSet.getLong(END_TIME));
        timetable.setDescription(resultSet.getString(TIMETABLE_DESCRIPTION));
        timetable.setWeekDay(WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY_COLUMN)));
        return timetable;
    }
    
    private PreparedStatement getPreparedStatementOfUpdate(PreparedStatement preparedStatement, 
                                                           TimetableEntity entity) throws SQLException {
        preparedStatement.setInt(1, entity.getGroup().getId());
        preparedStatement.setInt(2, entity.getCourse().getId());
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
