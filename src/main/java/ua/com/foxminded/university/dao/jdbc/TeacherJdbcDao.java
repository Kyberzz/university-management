package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TeacherMapper;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;

@Repository
public class TeacherJdbcDao implements TeacherDao {

    private static final String GET_COURSE_LIST_BY_TEACHER_ID = "teacher.getCourseListByTeacherId";
    private static final String DELETE_BY_ID = "teacher.deleteById";
    private static final String UPDATE = "teacher.update";
    private static final String INSERT = "teacher.insert";
    private static final String GET_BY_ID = "teacher.getById";
    
    private JdbcTemplate jdbcTemplate;
    private Environment queries;
    
    @Autowired
    public TeacherJdbcDao(JdbcTemplate jdbcTemplate, Environment teacherQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = teacherQueries;
    }

    @Override
    public TeacherEntity getCourseListByTeacherId(int id) {
        TeacherEntity teacherWithCourseList = jdbcTemplate.queryForObject(
                queries.getProperty(GET_COURSE_LIST_BY_TEACHER_ID), 
                (resultSet, rowNum) -> {
                    TeacherEntity teacher = null;
                    
                    if (teacher == null) {
                        TeacherMapper teacherMapper = new TeacherMapper();
                        teacher = teacherMapper.mapRow(resultSet, rowNum);
                        teacher.setCourseList(new ArrayList<>());
                    }

                    CourseMapper courseMapper = new CourseMapper();
                    CourseEntity course = courseMapper.mapRow(resultSet, rowNum);
                    teacher.getCourseList().add(course);
                    return teacher;
                }, 
                id);
        return teacherWithCourseList;
    }
    
    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(queries.getProperty(DELETE_BY_ID), 
                                   preparedStatement -> preparedStatement.setInt(1, id));
    }
    
    @Override
    public int update(TeacherEntity entity) {
        return jdbcTemplate.update(queries.getProperty(UPDATE), 
                                   preparedStatement -> getPreparedStatementOfUpdate(preparedStatement, 
                                                                                     entity));
    }
    
    @Override
    public TeacherEntity getById(int id) {
        TeacherEntity teacherEntity = jdbcTemplate.queryForObject(queries.getProperty(GET_BY_ID),
                (resultSet, rowNum) -> {
                    TeacherMapper teacherMapper = new TeacherMapper();
                    TeacherEntity teacher = teacherMapper.mapRow(resultSet, rowNum);
                    return teacher;
                },
                id);
        return teacherEntity;
    }
    
    @Override
    public int insert(TeacherEntity entity) {
        return jdbcTemplate.update(queries.getProperty(INSERT), 
                                   preparedStatement -> {
                                       preparedStatement.setString(1, entity.getFirstName());
                                       preparedStatement.setString(2, entity.getLastName());
                                   });
    }
    
    private PreparedStatement getPreparedStatementOfUpdate(PreparedStatement preparedStatement, 
                                                           TeacherEntity entity) throws SQLException {
        preparedStatement.setString(1, entity.getFirstName());
        preparedStatement.setString(2, entity.getLastName());
        preparedStatement.setInt(3, entity.getId());
        return preparedStatement;
    }
}
