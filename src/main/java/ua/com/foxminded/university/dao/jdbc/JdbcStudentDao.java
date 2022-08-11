package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;

public class JdbcStudentDao implements StudentDao {
    
    private static final String TEACHER_LAST_NAME = "teacher_last_name";
    private static final String TEACHER_FIRST_NAME = "teacher_first_name";
    private static final String TEACHER_ID = "teacher_id";
    private static final String COURSE_DESCRIPTION = "course_description";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_ID = "course_id";
    private static final String WEEK_DAY = "week_day";
    private static final String TIMETABLE_DESCRIPTION = "timetable_description";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String TIMETABLE_ID = "timetable_id";
    private static final String GROUP_NAME = "group_name";
    private static final String GET_STUDENT_BY_ID = "getStudentById";
    private static final String LAST_NAME = "student_last_name";
    private static final String FIRST_NAME = "student_first_name";
    private static final String GROUP_ID = "group_id";
    private static final String STUDENT_ID = "student_id";
    
    private Properties studentQuery;
    private JdbcTemplate jdbcTemplate;
    
    public JdbcStudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void setStudentQuery(Properties properties) {
        this.studentQuery = properties;
    }

    @Override
    public StudentEntity getStudentById(int id) {
            return jdbcTemplate.query(studentQuery.getProperty(GET_STUDENT_BY_ID), 
                    preparedStatement -> preparedStatement.setInt(1, id), 
                    resultSet -> {
                        StudentEntity student = null;
                        List<TimetableEntity> timetableList = new ArrayList<>();

                        while(resultSet.next()) {
                            student = new StudentEntity(resultSet.getInt(STUDENT_ID), 
                                                        resultSet.getString(FIRST_NAME), 
                                                        resultSet.getString(LAST_NAME));
                            GroupEntity group = new GroupEntity(resultSet.getInt(GROUP_ID), 
                                                                resultSet.getString(GROUP_NAME));
                            TimetableEntity timetable = new TimetableEntity(
                                    resultSet.getInt(TIMETABLE_ID), 
                                    resultSet.getLong(START_TIME),
                                    resultSet.getLong(END_TIME),
                                    resultSet.getString(TIMETABLE_DESCRIPTION),
                                    WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY)));
                            CourseEntity course = new CourseEntity(
                                    resultSet.getInt(COURSE_ID),
                                    resultSet.getString(COURSE_NAME),
                                    resultSet.getString(COURSE_DESCRIPTION));
                            TeacherEntity teacher = new TeacherEntity(
                                    resultSet.getInt(TEACHER_ID),
                                    resultSet.getString(TEACHER_FIRST_NAME),
                                    resultSet.getString(TEACHER_LAST_NAME));
                            course.setTeacher(teacher);
                            timetable.setCourse(course);
                            timetable.setGroup(group);
                            timetableList.add(timetable);
                            group.setTimetableList(timetableList);
                            student.setGroup(group);
                        }
                        return student;
                    });
    }
}
