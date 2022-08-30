package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;

@Repository
public class CourseJdbcDao implements CourseDao {
    
    private static final String WEEK_DAY = "week_day";
    private static final String TIMETABLE_DESCRIPTION = "timetable_description";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String GROUP_ID = "group_id";
    private static final String TIMETABLE_ID = "timetable_id";
    private static final String GET_TIMETABL_LIST_BY_COURSE_ID = "course.getTimetableListByCourseId";
    private static final String UPDATE = "course.update";
    private static final String COURSE_DESCRIPTION = "description";
    private static final String COURSE_NAME = "name";
    private static final String TEACHER_ID = "teacher_id";
    private static final String COURSE_ID = "id";
    private static final String GET_BY_ID = "course.getById";
    private static final String INSERT = "course.insert";
    private static final String DELETE_BY_ID = "course.deleteById";
    
    private JdbcTemplate jdbcTemplate;
    private Environment courseQueries;
    
    @Autowired
    public CourseJdbcDao(JdbcTemplate jdbcTemplate, Environment courseQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.courseQueries = courseQueries;
    }
    
    @Override
    public CourseEntity getTimetableListByCourseId(int id) {
        CourseEntity courseWithTimetableList = jdbcTemplate.query(
                courseQueries.getProperty(GET_TIMETABL_LIST_BY_COURSE_ID), 
                preparedStatement -> preparedStatement.setInt(1, id),
                resultSet -> {
                    return getCourseWithTimetableList(resultSet);
                });
        return courseWithTimetableList;
    }
    
    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(courseQueries.getProperty(DELETE_BY_ID),
                                   preparedStatement -> preparedStatement.setInt(1,id));
    }
    
    @Override
    public int update(CourseEntity entity) {
        return jdbcTemplate.update(courseQueries.getProperty(UPDATE),
                                   preparedStatement -> getPreparedStatementOfUpdate(preparedStatement, 
                                                                                     entity));
    }
    
    @Override
    public CourseEntity getById(int id) {
        CourseEntity courseEntity = jdbcTemplate.query(
                courseQueries.getProperty(GET_BY_ID), 
                preparedStatement -> preparedStatement.setInt(1, id),
                resultSet -> {
                    CourseEntity course = null;
                    
                    while(resultSet.next()) {
                        course = getCourseEntity(resultSet);
                    }
                    return course;
                });
        return courseEntity;
    }
    
    @Override
    public int insert(CourseEntity entity) {
        return jdbcTemplate.update(courseQueries.getProperty(INSERT),
                                   preparedStatement -> getPreparedStatementOfInsert(preparedStatement, 
                                                                                     entity));
    }
    
    private CourseEntity getCourseWithTimetableList(ResultSet resultSet) throws SQLException {
        CourseEntity course = null;
        TimetableEntity timetable = null;
        
        while(resultSet.next()) {
            if(course == null) {
                course = getCourseEntity(resultSet);
                course.setTimetableList(new ArrayList<>());
            } 
                                                                
            timetable = getTimetableEntity(resultSet);
            course.getTimetableList().add(timetable);
        }
        return course;
    }
    
    private TimetableEntity getTimetableEntity (ResultSet resultSet) throws SQLException {
        TimetableEntity timetable = new TimetableEntity();
        timetable.setId(resultSet.getInt(TIMETABLE_ID));
        timetable.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
        timetable.setCourse(new CourseEntity(resultSet.getInt(COURSE_ID)));
        timetable.setStartTime(resultSet.getLong(START_TIME));
        timetable.setEndTime(resultSet.getLong(END_TIME));
        timetable.setDescription(resultSet.getString(TIMETABLE_DESCRIPTION));
        timetable.setWeekDay(WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY)));
        return timetable;
    }

    private PreparedStatement getPreparedStatementOfUpdate(PreparedStatement preparedStatement, 
                                                           CourseEntity entity) throws SQLException {
        preparedStatement.setInt(1, entity.getTeacher().getId()); 
        preparedStatement.setString(2, entity.getName());
        preparedStatement.setString(3, entity.getDescription());
        preparedStatement.setInt(4, entity.getId());
        return preparedStatement;
    }
    
    private CourseEntity getCourseEntity(ResultSet resultSet) throws SQLException {
        CourseEntity course = new CourseEntity();
        course.setId(resultSet.getInt(COURSE_ID));
        course.setTeacher(new TeacherEntity(resultSet.getInt(TEACHER_ID)));
        course.setName(resultSet.getString(COURSE_NAME));
        course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
        return course;
    }
    
    private PreparedStatement getPreparedStatementOfInsert(PreparedStatement preparedStatement, 
                                                           CourseEntity entity) throws SQLException {
        preparedStatement.setInt(1, entity.getTeacher().getId()); 
        preparedStatement.setString(2, entity.getName());
        preparedStatement.setString(3, entity.getDescription());
        return preparedStatement;
    }
}
