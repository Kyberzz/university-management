package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

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
    private static final String GET_TIMETABLES_BY_COURSE_ID = "course.getTimetablesByCourseId";
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

    public CourseEntity getTimetableListByCourseId(int id) {
        return jdbcTemplate.query(courseQueries.getProperty(GET_TIMETABLES_BY_COURSE_ID), 
                                  preparedStatement -> preparedStatement.setInt(1, id),
                                  resultSet -> {
                                      List<TimetableEntity> timetableList = new ArrayList<>();
                                      CourseEntity course = new CourseEntity();
                                      TimetableEntity timetable = new TimetableEntity();
                                      
                                      while(resultSet.next()) {
                                          if(resultSet.getInt(COURSE_ID) != course.getId()) {
                                              course.setId(resultSet.getInt(COURSE_ID));
                                              course.setTeacher(new TeacherEntity(resultSet.getInt(TEACHER_ID)));
                                              course.setName(resultSet.getString(COURSE_NAME));
                                              course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                                          }
                                          
                                          timetable.setId(resultSet.getInt(TIMETABLE_ID));
                                          timetable.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
                                          timetable.setCourse(course);
                                          timetable.setStartTime(resultSet.getLong(START_TIME));
                                          timetable.setEndTime(resultSet.getLong(END_TIME));
                                          timetable.setDescription(resultSet.getString(TIMETABLE_DESCRIPTION));
                                          timetable.setWeekDay(WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY)));
                                          timetableList.add(timetable);
                                          course.setTimetableList(timetableList);
                                      }
                                      return course;
                                  });
    }
    
    public int deleteById(int id) {
        return jdbcTemplate.update(courseQueries.getProperty(DELETE_BY_ID),
                                   preparedStatement -> preparedStatement.setInt(1,id));
    }
    
    public int update(CourseEntity entity) {
        return jdbcTemplate.update(courseQueries.getProperty(UPDATE),
                                   preparedStatement -> {
                                       preparedStatement.setInt(1, entity.getTeacher().getId());
                                       preparedStatement.setString(2, entity.getName());
                                       preparedStatement.setString(3, entity.getDescription());
                                       preparedStatement.setInt(4, entity.getId());
                                   });
    }
    
    public CourseEntity getById(int id) {
        return jdbcTemplate.query(courseQueries.getProperty(GET_BY_ID), 
                                  preparedStatement -> preparedStatement.setInt(1, id),
                                  resultSet -> {
                                      CourseEntity course = new CourseEntity();
                                      
                                      while(resultSet.next()) {
                                          course.setId(resultSet.getInt(COURSE_ID));
                                          course.setTeacher(new TeacherEntity(resultSet.getInt(TEACHER_ID)));
                                          course.setName(resultSet.getString(COURSE_NAME));
                                          course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                                      }
                                      return course;
                                  });
    }
    
    public int insert(CourseEntity entity) {
        return jdbcTemplate.update(courseQueries.getProperty(INSERT),
                                   preparedStatement -> {
                                       preparedStatement.setInt(1, entity.getTeacher().getId());
                                       preparedStatement.setString(2, entity.getName());
                                       preparedStatement.setString(3, entity.getDescription());
                                   });
    }
}
