package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;

public class JdbcTeacherDao implements TeacherDao {

    private static final String WEEK_DAY = "week_day";
    private static final String TIMETABLE_DESCRIPTION = "timetable_description";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String GROUP_ID = "group_id";
    private static final String TIMETABLE_ID = "timetable_id";
    private static final String DESCRIPTION = "description";
    private static final String COURSE_NAME = "name";
    private static final String GET_COURSE_LIST_BY_TEACHER_ID = "getCourseListByTeacherId";
    private static final String DELETE_BY_ID = "deleteById";
    private static final String UPDATE = "update";
    private static final String INSERT = "insert";
    private static final String LAST_NAME = "last_name";
    private static final String FIRST_NAME = "first_name";
    private static final String COURSE_ID = "id";
    private static final String TEACHER_ID = "id";
    private static final String GET_TEACHER_BY_ID = "getTeacherById";

    Properties teacherQueries;
    JdbcTemplate jdbcTemplate;

    public JdbcTeacherDao(Properties teacherQueries, JdbcTemplate jdbcTemplate) {
        this.teacherQueries = teacherQueries;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public TeacherEntity getCourseListByTeacherId(int id) {
       return jdbcTemplate.query(teacherQueries.getProperty(GET_COURSE_LIST_BY_TEACHER_ID),
                                 preparedStatement -> preparedStatement.setInt(1, id), 
                                 resultSet -> {
                                     List<CourseEntity> courseList = new ArrayList<>();
                                     List<TimetableEntity> timetableList = new ArrayList<>();
                                     CourseEntity course = new CourseEntity();
                                     TeacherEntity teacher = new TeacherEntity();
                                     
                                     while(resultSet.next()) {
                                         
                                         TimetableEntity timetable = new TimetableEntity();
                                         timetable.setId(resultSet.getInt(TIMETABLE_ID));
                                         timetable.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
                                         timetable.setCourse(new CourseEntity(resultSet.getInt(COURSE_ID)));
                                         timetable.setStartTime(resultSet.getLong(START_TIME));
                                         timetable.setEndTime(resultSet.getLong(END_TIME));
                                         timetable.setDescription(resultSet.getString(TIMETABLE_DESCRIPTION));
                                         timetable.setWeekDay(WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY)));
                                         timetableList.add(timetable);
                                         
                                         if (resultSet.getInt(COURSE_ID) != course.getId()) {
                                             course.setName(resultSet.getString(COURSE_NAME));
                                             course.setTeacher(teacher);
                                             course.setDescription(resultSet.getString(DESCRIPTION));
                                         } else if (resultSet.getInt(TEACHER_ID) != teacher.getId()) {
                                             teacher.setId(resultSet.getInt(TEACHER_ID));
                                             teacher.setFirstName(resultSet.getString(FIRST_NAME));
                                             teacher.setLastName(resultSet.getString(LAST_NAME));
                                         }
                                         
                                         course.setTimetableList(timetableList);
                                         courseList.add(course);
                                         teacher.setCourseList(courseList);
                                     }
                                     return teacher;
                                 });
    }
    
    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(teacherQueries.getProperty(DELETE_BY_ID), 
                                   preparedStatement -> preparedStatement.setInt(1, id));
    }
    
    @Override
    public int update(TeacherEntity entity) {
        return jdbcTemplate.update(teacherQueries.getProperty(UPDATE), 
                                   preparedStatement -> {
                                       preparedStatement.setString(1, entity.getFirstName());
                                       preparedStatement.setString(2, entity.getLastName());
                                       preparedStatement.setInt(3, entity.getId());
                                   });
    }
    
    @Override
    public TeacherEntity getById(int id) {
        return jdbcTemplate.query(teacherQueries.getProperty(GET_TEACHER_BY_ID),
                                  preparedStatement -> preparedStatement.setInt(1, id), 
                                  resultSet -> {
                                      TeacherEntity teacher = new TeacherEntity();

                                      while (resultSet.next()) {
                                          teacher.setId(resultSet.getInt(TEACHER_ID));
                                          teacher.setFirstName(resultSet.getString(FIRST_NAME));
                                          teacher.setLastName(resultSet.getString(LAST_NAME));
                                      }
                                      return teacher;
                                  });
    }
    
    @Override
    public int insert(TeacherEntity entity) {
        return jdbcTemplate.update(teacherQueries.getProperty(INSERT), 
                                   preparedStatement -> {
                                       preparedStatement.setString(1, entity.getFirstName());
                                       preparedStatement.setString(2, entity.getLastName());
                                   });
    }
}
