package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TimetableMapper;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

@Repository
public class CourseJdbcDao implements CourseDao {
    
    private static final String GET_TIMETABL_LIST_BY_COURSE_ID = "course.getTimetableListByCourseId";
    private static final String UPDATE = "course.update";
    private static final String GET_BY_ID = "course.getById";
    private static final String INSERT = "course.insert";
    private static final String DELETE_BY_ID = "course.deleteById";
    
    private JdbcTemplate jdbcTemplate;
    private Environment queries;
    private CourseMapper courseMapper;
    private TimetableMapper timetableMapper;
    
    @Autowired
    public CourseJdbcDao(JdbcTemplate jdbcTemplate, Environment queries, CourseMapper courseMapper,
            TimetableMapper timetableMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.courseMapper = courseMapper;
        this.timetableMapper = timetableMapper;
    }

    @Override
    public CourseEntity getTimetableListByCourseId(int id) {
        String sqlGetTimetableListByCourseId = queries.getProperty(GET_TIMETABL_LIST_BY_COURSE_ID);
        CourseEntity courseWhithTimetableList = jdbcTemplate.queryForObject(
                sqlGetTimetableListByCourseId,                                   
                (resultSet, rowNum) -> {
                    CourseEntity course = null;
                    if (course == null) {
                        course = courseMapper.mapRow(resultSet, rowNum);
                        course.setTimetableList(new ArrayList<>());
                    }
                    
                    TimetableEntity timetable = timetableMapper.mapRow(resultSet, id);
                    course.getTimetableList().add(timetable);
                    return course;
                },
                id);
        return courseWhithTimetableList;
    }
    
    @Override
    public int deleteById(int id) {
        String sqlDeleteCourseById = queries.getProperty(DELETE_BY_ID);
        return jdbcTemplate.update(sqlDeleteCourseById,
                                   preparedStatement -> preparedStatement.setInt(1,id));
    }
    
    @Override
    public int update(CourseEntity entity) {
        String sqlUpdateCourse = queries.getProperty(UPDATE);
        return jdbcTemplate.update(sqlUpdateCourse,
                                   preparedStatement -> getPreparedStatementOfUpdate(preparedStatement, 
                                                                                     entity));
    }
    
    @Override
    public CourseEntity getById(int id) {
        String sqlGetCourseById = queries.getProperty(GET_BY_ID);
        CourseEntity courseEntity = jdbcTemplate.queryForObject(sqlGetCourseById, 
                (resultSet, rowNum) -> courseMapper.mapRow(resultSet, rowNum), 
                id);
        return courseEntity;
    }
    
    @Override
    public int insert(CourseEntity entity) {
        String sqlInsertCourse = queries.getProperty(INSERT);
        return jdbcTemplate.update(sqlInsertCourse,
                                   preparedStatement -> getPreparedStatementOfInsert(preparedStatement, 
                                                                                     entity));
    }
    
    private PreparedStatement getPreparedStatementOfUpdate(PreparedStatement preparedStatement, 
                                                           CourseEntity entity) throws SQLException {
        preparedStatement.setObject(1, entity.getTeacher().getId()); 
        preparedStatement.setString(2, entity.getName());
        preparedStatement.setString(3, entity.getDescription());
        preparedStatement.setInt(4, entity.getId());
        return preparedStatement;
    }
    
    private PreparedStatement getPreparedStatementOfInsert(PreparedStatement preparedStatement, 
                                                           CourseEntity entity) throws SQLException {
        preparedStatement.setObject(1, entity.getTeacher().getId()); 
        preparedStatement.setString(2, entity.getName());
        preparedStatement.setString(3, entity.getDescription());
        return preparedStatement;
    }
}
