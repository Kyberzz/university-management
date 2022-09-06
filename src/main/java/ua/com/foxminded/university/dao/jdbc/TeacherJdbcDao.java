package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

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
    private TeacherMapper teacherMapper;
    private CourseMapper courseMapper;

    public TeacherJdbcDao(JdbcTemplate jdbcTemplate, Environment queries, TeacherMapper teacherMapper,
            CourseMapper courseMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.teacherMapper = teacherMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    public TeacherEntity getCourseListByTeacherId(int id) {
        String sqlGetCourseListByTeacherId = queries.getProperty(GET_COURSE_LIST_BY_TEACHER_ID);
        TeacherEntity teacherWithCourseList = jdbcTemplate.queryForObject(
                sqlGetCourseListByTeacherId, 
                (resultSet, rowNum) -> {
                    TeacherEntity teacher = null;
                    
                    if (teacher == null) {
                        teacher = teacherMapper.mapRow(resultSet, rowNum);
                        teacher.setCourseList(new ArrayList<>());
                    }

                    CourseEntity course = courseMapper.mapRow(resultSet, rowNum);
                    teacher.getCourseList().add(course);
                    return teacher;
                }, 
                id);
        return teacherWithCourseList;
    }
    
    @Override
    public int deleteById(int id) {
        String sqlDeleteTeacherByid = queries.getProperty(DELETE_BY_ID);
        return jdbcTemplate.update(sqlDeleteTeacherByid, 
                                   preparedStatement -> preparedStatement.setInt(1, id));
    }
    
    @Override
    public int update(TeacherEntity entity) {
        String sqlUpdateTeacher = queries.getProperty(UPDATE);
        return jdbcTemplate.update(sqlUpdateTeacher, 
                                   preparedStatement -> getPreparedStatementOfUpdate(preparedStatement, 
                                                                                     entity));
    }
    
    @Override
    public TeacherEntity getById(int id) {
        String sqlGetTeacherById = queries.getProperty(GET_BY_ID);
        TeacherEntity teacherEntity = jdbcTemplate.queryForObject(sqlGetTeacherById,
                (resultSet, rowNum) -> teacherMapper.mapRow(resultSet, rowNum),
                id);
        return teacherEntity;
    }
    
    @Override
    public int insert(TeacherEntity entity) {
        String sqlInsertTeacher = queries.getProperty(INSERT);
        return jdbcTemplate.update(sqlInsertTeacher, 
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
