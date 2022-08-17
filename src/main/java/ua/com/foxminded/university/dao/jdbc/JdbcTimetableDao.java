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
    
    private static final String UPDATE = "update";
    private static final String DELETE_BY_ID = "deleteById";
    private static final String GET_BY_ID = "getById";
    private static final String INSERT = "insert";
    private static final String GET_TIMETABLE_BY_TEACHER_ID = "getTimetableByTeacherId";
    private static final String WEEK_DAY = "week_day";
    private static final String DESCRIPTION = "description";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String GROUP_ID = "group_id";
    private static final String COURSE_ID = "course_id";
    private static final String TIMETABLE_ID = "id";
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
    
    @Override
    public TimetableEntity getById(int id) {
        return jdbcTemplate.query(timetableQueries.getProperty(GET_BY_ID), 
                                  preparedStatament -> preparedStatament.setInt(1, id), 
                                  resultSet -> {
                                     TimetableEntity timetable = new TimetableEntity();
                                     
                                     while(resultSet.next()) {
                                         timetable.setId(resultSet.getInt(TIMETABLE_ID));
                                         timetable.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
                                         timetable.setCourse(new CourseEntity(resultSet.getInt(COURSE_ID)));
                                         timetable.setStartTime(resultSet.getLong(START_TIME));
                                         timetable.setEndTime(resultSet.getLong(END_TIME));
                                         timetable.setDescription(resultSet.getString(DESCRIPTION));
                                         timetable.setWeekDay(WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY)));
                                     }
                                     return timetable;
                                  });
    }
    
    @Override
    public int update(TimetableEntity entity) {
        return jdbcTemplate.update(timetableQueries.getProperty(UPDATE),
                                   preparedStatement -> {
                                       preparedStatement.setInt(1, entity.getGroup().getId());
                                       preparedStatement.setInt(2, entity.getCourse().getId());
                                       preparedStatement.setLong(3, entity.getStartTime());
                                       preparedStatement.setLong(4, entity.getEndTime());
                                       preparedStatement.setString(5, entity.getDescription());
                                       preparedStatement.setString(6, entity.getWeekDay().toString());
                                   });
    }
    
    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(timetableQueries.getProperty(DELETE_BY_ID),
                                   preparedStatement -> preparedStatement.setInt(1, id));
    }
    
    @Override
    public int insert(TimetableEntity entity) {
        return jdbcTemplate.update(timetableQueries.getProperty(INSERT),
                                   preparedStatement -> {
                                       preparedStatement.setInt(1, entity.getGroup().getId());
                                       preparedStatement.setInt(2, entity.getCourse().getId());
                                       preparedStatement.setLong(3, entity.getStartTime());
                                       preparedStatement.setLong(4, entity.getEndTime());
                                       preparedStatement.setString(5, entity.getDescription());
                                       preparedStatement.setString(6, entity.getWeekDay().toString());
                                   });
    }
}
