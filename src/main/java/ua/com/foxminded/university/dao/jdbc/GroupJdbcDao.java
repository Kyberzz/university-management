package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;

public class GroupJdbcDao implements GroupDao {
    
    private static final String WEEK_DAY = "week_day";
    private static final String TIMETABLE_DESCRIPTION = "timetable_description";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String COURSE_ID = "course_id";
    private static final String TIMETABLE_ID = "timetable_id";
    private static final String GET_TIMETABLE_LIST_BY_GROUP_ID = "getTimetableListByGroupId";
    private static final String LAST_NAME = "last_name";
    private static final String STUDENT_FIRST_NAME = "first_name";
    private static final String STUDENT_ID = "student_id";
    private static final String GET_STUDENT_LIST_BY_GROUP_ID = "getStudentsByGroupId";
    private static final String INSERT = "insert";
    private static final String GROUP_NAME = "name";
    private static final String GROUP_ID = "id";
    private static final String GET_BY_ID = "getById";
    private static final String UPDATE = "update";
    private static final String DELETE_BY_ID = "deleteById";
    
    private Environment groupQueries;
    private JdbcTemplate jdbcTemplate;
    
    public GroupJdbcDao(Environment groupQueries, JdbcTemplate jdbcTemplate) {
        this.groupQueries = groupQueries;
        this.jdbcTemplate = jdbcTemplate;
    }

    public GroupEntity getTimetableListByGroupId(int id) {
        return jdbcTemplate.query(groupQueries.getProperty(GET_TIMETABLE_LIST_BY_GROUP_ID), 
                                  preparedStatement -> preparedStatement.setInt(1, id),
                                  resultSet -> {
                                      List<TimetableEntity> timetableList = new ArrayList<>();
                                      GroupEntity group = new GroupEntity();
                                      TimetableEntity timetable = new TimetableEntity();
                                      
                                      while(resultSet.next()) {
                                          if (resultSet.getInt(GROUP_ID) != group.getId()) {
                                              group.setId(resultSet.getInt(GROUP_ID));
                                              group.setName(resultSet.getString(GROUP_NAME));
                                              timetable.setGroup(group);
                                          }
                                          
                                          timetable.setId(resultSet.getInt(TIMETABLE_ID));
                                          timetable.setCourse(new CourseEntity(resultSet.getInt(COURSE_ID)));
                                          timetable.setStartTime(resultSet.getLong(START_TIME));
                                          timetable.setEndTime(resultSet.getLong(END_TIME));
                                          timetable.setDescription(resultSet.getString(TIMETABLE_DESCRIPTION));
                                          timetable.setWeekDay(WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY)));
                                          timetableList.add(timetable);
                                          group.setTimetableList(timetableList);
                                      }
                                      return group;
                                  });
    }
    
    public GroupEntity getStudentListByGroupId(int id) {
        return jdbcTemplate.query(groupQueries.getProperty(GET_STUDENT_LIST_BY_GROUP_ID),
                                  preparedStatement -> preparedStatement.setInt(1, id),
                                  resultSet -> {
                                      List<StudentEntity> studentList = new ArrayList<>();
                                      GroupEntity group = new GroupEntity();
                                      StudentEntity student = new StudentEntity();
                                      
                                      while(resultSet.next()) {
                                          if (resultSet.getInt(GROUP_ID) != group.getId()) {
                                              group.setId(resultSet.getInt(GROUP_ID));
                                              group.setName(resultSet.getString(GROUP_NAME));
                                              student.setGroup(group);
                                          }
                                          
                                          student.setId(resultSet.getInt(STUDENT_ID));
                                          student.setFirstName(resultSet.getString(STUDENT_FIRST_NAME));
                                          student.setLastName(resultSet.getString(LAST_NAME));
                                          studentList.add(student);
                                          group.setStudentList(studentList);
                                      }
                                      return group;
                                  });
    }
    
    public int insert(GroupEntity entity) {
        return jdbcTemplate.update(groupQueries.getProperty(INSERT),
                                   preparedStatement -> preparedStatement.setString(1, entity.getName()));
    }
    
    public GroupEntity getById(int id) {
        return jdbcTemplate.query(groupQueries.getProperty(GET_BY_ID), 
                                  preparedStatement -> preparedStatement.setInt(1, id),
                                  resultSet -> {
                                      GroupEntity group = new GroupEntity();
                                      
                                      while(resultSet.next()) {
                                          group.setId(resultSet.getInt(GROUP_ID));
                                          group.setName(resultSet.getString(GROUP_NAME));
                                      }
                                      return group;
                                  });
    }
    
    public int update(GroupEntity entity) {
        return jdbcTemplate.update(groupQueries.getProperty(UPDATE),
                                   preparedStatement -> {
                                       preparedStatement.setString(1, entity.getName());
                                       preparedStatement.setInt(2, entity.getId());
                                   });
    }
    
    public int deleteById(int id) {
        return jdbcTemplate.update(groupQueries.getProperty(DELETE_BY_ID), 
                                   preparedStatement -> preparedStatement.setInt(1, id));
    }
}
