package ua.com.foxminded.university.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;

@Repository
public class GroupJdbcDao implements GroupDao {
    
    private static final String WEEK_DAY = "week_day";
    private static final String TIMETABLE_DESCRIPTION = "timetable_description";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String COURSE_ID = "course_id";
    private static final String TIMETABLE_ID = "timetable_id";
    private static final String GET_TIMETABLE_LIST_BY_GROUP_ID = "group.getTimetableListByGroupId";
    private static final String LAST_NAME = "last_name";
    private static final String STUDENT_FIRST_NAME = "first_name";
    private static final String STUDENT_ID = "student_id";
    private static final String GET_STUDENT_LIST_BY_GROUP_ID = "group.getStudentListByGroupId";
    private static final String INSERT = "group.insert";
    private static final String GROUP_NAME = "name";
    private static final String GROUP_ID = "id";
    private static final String GET_BY_ID = "group.getById";
    private static final String UPDATE = "group.update";
    private static final String DELETE_BY_ID = "group.deleteById";
    
    private Environment groupQueries;
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public GroupJdbcDao(Environment groupQueries, JdbcTemplate jdbcTemplate) {
        this.groupQueries = groupQueries;
        this.jdbcTemplate = jdbcTemplate;
    }

    public GroupEntity getTimetableListByGroupId(int id) {
        GroupEntity groupWithTimetableList = jdbcTemplate.query(
                groupQueries.getProperty(GET_TIMETABLE_LIST_BY_GROUP_ID),
                preparedStatement -> preparedStatement.setInt(1, id), 
                resultSet -> {
                    GroupEntity group = null;
                    TimetableEntity timetable = null;

                    while (resultSet.next()) {
                        if (group == null) {
                            group = new GroupEntity();
                            group.setId(resultSet.getInt(GROUP_ID));
                            group.setName(resultSet.getString(GROUP_NAME));
                            group.setTimetableList(new ArrayList<>());
                        }

                        timetable = getTimetableEntity(resultSet);
                        group.getTimetableList().add(timetable);
                    }
                    return group;
                });
        return groupWithTimetableList;
    }
    
    public GroupEntity getStudentListByGroupId(int id) {
        
        GroupEntity groupWithStudentList = jdbcTemplate.query(
                groupQueries.getProperty(GET_STUDENT_LIST_BY_GROUP_ID),
                preparedStatement -> preparedStatement.setInt(1, id), 
                resultSet -> {
                    GroupEntity group = null;
                    StudentEntity student = null;

                    while (resultSet.next()) {
                        if (group == null) {
                            group = getGroupEntity(resultSet);
                            group.setStudentList(new ArrayList<>());
                        }

                        student = getStudentEntity(resultSet);
                        group.getStudentList().add(student);
                    }
                    return group;
                });
        return groupWithStudentList;
    }
    
    public int insert(GroupEntity entity) {
        return jdbcTemplate.update(groupQueries.getProperty(INSERT),
                                   preparedStatement -> preparedStatement.setString(1, entity.getName()));
    }
    
    public GroupEntity getById(int id) {
        GroupEntity groupEntity = jdbcTemplate.query(groupQueries.getProperty(GET_BY_ID), 
                                  preparedStatement -> preparedStatement.setInt(1, id),
                                  resultSet -> {
                                      GroupEntity group = new GroupEntity();
                                      
                                      while(resultSet.next()) {
                                          group.setId(resultSet.getInt(GROUP_ID));
                                          group.setName(resultSet.getString(GROUP_NAME));
                                      }
                                      return group;
                                  });
        return groupEntity;
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
    
    private TimetableEntity getTimetableEntity(ResultSet resultSet) throws SQLException {
        TimetableEntity timetable = new TimetableEntity();
        timetable.setId(resultSet.getInt(TIMETABLE_ID));
        timetable.setCourse(new CourseEntity(resultSet.getInt(COURSE_ID)));
        timetable.setStartTime(resultSet.getLong(START_TIME));
        timetable.setEndTime(resultSet.getLong(END_TIME));
        timetable.setDescription(resultSet.getString(TIMETABLE_DESCRIPTION));
        timetable.setWeekDay(WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY)));
        timetable.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
        return timetable;
    }
    
    private StudentEntity getStudentEntity(ResultSet resultSet) throws SQLException {
        StudentEntity student = new StudentEntity();
        student.setId(resultSet.getInt(STUDENT_ID));
        student.setFirstName(resultSet.getString(STUDENT_FIRST_NAME));
        student.setLastName(resultSet.getString(LAST_NAME));
        student.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
        return student;
    }
    
    private GroupEntity getGroupEntity(ResultSet resultSet) throws SQLException {
        GroupEntity group = new GroupEntity();
        group.setId(resultSet.getInt(GROUP_ID));
        group.setName(resultSet.getString(GROUP_NAME));
        return group;
    }
}
