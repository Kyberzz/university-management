package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;

public class JdbcTimetableDao implements TimetableDao {
    
    private static final String GET_TIMETABLE_BY_TEACHER_ID = "getTimetableByTeacherId";
    private static final String WEEK_DAY = "week_day";
    private static final String DESCRIPTION = "description";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String GROUP_ID = "group_id";
    private static final String COURSE_ID = "course_id";
    private static final String TIMETABLE_ID = "timetable_id";
    private static final String GET_TIMETABLE_BY_STUDENT_ID = "getTimetableByStudentId";
    
    JdbcTemplate jdbcTemplate;
    Properties timetableQueries;
    
    public JdbcTimetableDao(JdbcTemplate jdbcTemplate, Properties timetableQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.timetableQueries = timetableQueries;
    }
    
    
    

    @Override
    public List<TimetableEntity> getTimetableByStudentId(int id) {
        return jdbcTemplate.query(timetableQueries.getProperty(GET_TIMETABLE_BY_STUDENT_ID), 
                preparedStatement -> preparedStatement.setInt(1, id), 
                resultSet -> {
                    List<TimetableEntity> timetableList = new ArrayList<>();
                    while(resultSet.next()) {
                        TimetableEntity timetable = new TimetableEntity();
                        timetable.setId(resultSet.getInt(TIMETABLE_ID));
                        timetable.setCourse(new CourseEntity(resultSet.getInt(COURSE_ID)));
                        timetable.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
                        timetable.setStartTime(resultSet.getLong(START_TIME));
                        timetable.setEndTime(resultSet.getLong(END_TIME));
                        timetable.setDescription(resultSet.getString(DESCRIPTION));
                        timetable.setWeekDay(WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY)));
                        timetableList.add(timetable);
                    }
                    return timetableList;
                });
    }
    
    @Override
    public List<TimetableEntity> getTimetableByTeacherId(int id) {
        return jdbcTemplate.query(timetableQueries.getProperty(GET_TIMETABLE_BY_TEACHER_ID), 
                peparedStatement -> peparedStatement.setInt(1, id), 
                resultSet -> {
                    List<TimetableEntity> timetableList = new ArrayList<>();
                    while(resultSet.next()) {
                        TimetableEntity timetable = new TimetableEntity();
                        timetable.setId(resultSet.getInt(TIMETABLE_ID));
                        timetable.setCourse(new CourseEntity(resultSet.getInt(COURSE_ID)));
                        timetable.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
                        timetable.setStartTime(resultSet.getLong(START_TIME));
                        timetable.setEndTime(resultSet.getLong(END_TIME));
                        timetable.setDescription(resultSet.getString(DESCRIPTION));
                        timetable.setWeekDay(WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY)));
                        timetableList.add(timetable);
                    }
                    return timetableList;
                });
    }
    
    public T getById(int id);
    public int update(T entity);
    public int deleteById(int id);
    
    @Override
    public int insert(TimetableEntity entity) {
        return 
        
    } 
}
