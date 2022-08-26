package ua.com.foxminded.university.dao.jdbc;

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
    private static final String WEEK_DAY = "week_day";
    private static final String TIMETABLE_DESCRIPTION = "description";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String GROUP_ID = "group_id";
    private static final String TEACHER_ID = "teacher_id";
    private static final String COURSE_DESCRIPTION = "course_description";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_ID = "course_id";
    private static final String TIMETABLE_ID = "id";
    
    JdbcTemplate jdbcTemplate;
    Environment timetableQueries;
    
    @Autowired
    public TimetableJdbcDao(JdbcTemplate jdbcTemplate, Environment timetableQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.timetableQueries = timetableQueries;
    }

    public TimetableEntity getCourseByTimetableId(int id) {
        return jdbcTemplate.query(timetableQueries.getProperty(GET_COURCE_BY_TIMETABLE_ID),
                                  preparedStatement -> preparedStatement.setInt(1, id),
                                  resultSet -> {
                                      TimetableEntity timetable = new TimetableEntity();
                                      CourseEntity course = new CourseEntity();
                                      
                                      while (resultSet.next()) {
                                          course.setId(resultSet.getInt(COURSE_ID));
                                          course.setName(resultSet.getString(COURSE_NAME));
                                          course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                                          course.setTeacher(new TeacherEntity(resultSet.getInt(TEACHER_ID)));
                                          timetable.setCourse(course);
                                          timetable.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
                                          timetable.setId(resultSet.getInt(TIMETABLE_ID));
                                          timetable.setStartTime(resultSet.getLong(START_TIME));
                                          timetable.setEndTime(resultSet.getLong(END_TIME));
                                          timetable.setDescription(resultSet.getString(TIMETABLE_DESCRIPTION));
                                      }
                                      return timetable;
                                  });
    }
    
    @Override
    public TimetableEntity getGroupByTimetableId(int id) {
        return jdbcTemplate.query(timetableQueries.getProperty(GET_GROUP_BY_TIMETABLE_ID), 
                                  preparedStatement -> preparedStatement.setInt(1, id), 
                                  resultSet -> {
                                      TimetableEntity timetable = new TimetableEntity();
                                      GroupEntity group = new GroupEntity();
                                      
                                      while (resultSet.next()) {
                                          group.setId(resultSet.getInt(GROUP_ID));
                                          group.setName(resultSet.getString(GROUP_NAME));
                                          timetable.setGroup(group);
                                          timetable.setId(resultSet.getInt(TIMETABLE_ID));
                                          timetable.setStartTime(resultSet.getLong(START_TIME));
                                          timetable.setEndTime(resultSet.getLong(END_TIME));
                                          timetable.setCourse(new CourseEntity(resultSet.getInt(COURSE_ID)));
                                          timetable.setDescription(resultSet.getString(TIMETABLE_DESCRIPTION));
                                          timetable.setWeekDay(WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY)));
                                      }
                                      return timetable;
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
                                         timetable.setDescription(resultSet.getString(TIMETABLE_DESCRIPTION));
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
