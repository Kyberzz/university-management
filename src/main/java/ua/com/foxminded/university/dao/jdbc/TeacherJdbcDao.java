package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;

@Repository
public class TeacherJdbcDao implements TeacherDao {

    private static final String COURSE_DESCRIPTION = "course_description";
    private static final String COURSE_NAME = "course_name";
    private static final String GET_COURSE_LIST_BY_TEACHER_ID = "teacher.getCourseListByTeacherId";
    private static final String DELETE_BY_ID = "teacher.deleteById";
    private static final String UPDATE = "teacher.update";
    private static final String INSERT = "teacher.insert";
    private static final String LAST_NAME = "last_name";
    private static final String FIRST_NAME = "first_name";
    private static final String COURSE_ID = "course_id";
    private static final String TEACHER_ID = "id";
    private static final String GET_BY_ID = "teacher.getById";
    
    JdbcTemplate jdbcTemplate;
    Environment teacherQueries;
    
    @Autowired
    public TeacherJdbcDao(JdbcTemplate jdbcTemplate, Environment teacherQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherQueries = teacherQueries;
    }

    @Override
    public TeacherEntity getCourseListByTeacherId(int id) {
        TeacherEntity teacherWithCourseList = jdbcTemplate.query(
                teacherQueries.getProperty(GET_COURSE_LIST_BY_TEACHER_ID),
                preparedStatement -> preparedStatement.setInt(1, id), 
                resultSet -> {
                    CourseEntity course = null;
                    TeacherEntity teacher = null;

                    while (resultSet.next()) {
                        if (teacher == null) {
                            teacher = getTeacherEntity(resultSet);
                            teacher.setCourseList(new ArrayList<>());
                        }

                        course = getCourseEntity(resultSet);
                        teacher.getCourseList().add(course);
                    }
                    return teacher;
                });
        return teacherWithCourseList;
    }
    
    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(teacherQueries.getProperty(DELETE_BY_ID), 
                                   preparedStatement -> preparedStatement.setInt(1, id));
    }
    
    @Override
    public int update(TeacherEntity entity) {
        return jdbcTemplate.update(teacherQueries.getProperty(UPDATE), 
                                   preparedStatement -> getPreparedStatementOfUpdate(preparedStatement, 
                                                                                     entity));
    }
    
    @Override
    public TeacherEntity getById(int id) {
        TeacherEntity teacherEntity = jdbcTemplate.query(
                teacherQueries.getProperty(GET_BY_ID),
                preparedStatement -> preparedStatement.setInt(1, id), 
                resultSet -> {
                    TeacherEntity teacher = null;

                    while (resultSet.next()) {
                        teacher = getTeacherEntity(resultSet);
                    }
                    return teacher;
                });
        return teacherEntity;
    }
    
    @Override
    public int insert(TeacherEntity entity) {
        return jdbcTemplate.update(teacherQueries.getProperty(INSERT), 
                                   preparedStatement -> {
                                       preparedStatement.setString(1, entity.getFirstName());
                                       preparedStatement.setString(2, entity.getLastName());
                                   });
    }
    
    private CourseEntity getCourseEntity(ResultSet resultSet) throws SQLException {
        CourseEntity course = new CourseEntity();
        course.setId(resultSet.getInt(COURSE_ID));
        course.setName(resultSet.getString(COURSE_NAME));
        course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
        course.setTeacher(new TeacherEntity(resultSet.getInt(TEACHER_ID)));
        return course;
    }
    
    private PreparedStatement getPreparedStatementOfUpdate(PreparedStatement preparedStatement, 
                                                           TeacherEntity entity) throws SQLException {
        preparedStatement.setString(1, entity.getFirstName());
        preparedStatement.setString(2, entity.getLastName());
        preparedStatement.setInt(3, entity.getId());
        return preparedStatement;
    }
    
    private TeacherEntity getTeacherEntity(ResultSet resultSet) throws SQLException {
        TeacherEntity teacher = new TeacherEntity();
        teacher.setId(resultSet.getInt(TEACHER_ID));
        teacher.setFirstName(resultSet.getString(FIRST_NAME));
        teacher.setLastName(resultSet.getString(LAST_NAME));
        return teacher;
    }
}
