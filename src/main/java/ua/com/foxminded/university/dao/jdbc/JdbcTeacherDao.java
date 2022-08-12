package ua.com.foxminded.university.dao.jdbc;

import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.TeacherEntity;

public class JdbcTeacherDao implements TeacherDao<TeacherEntity> {
    
    private static final String LAST_NAME = "last_name";
    private static final String FIRST_NAME = "first_name";
    private static final String TEACHER_ID = "id";
    private static final String GET_TEACHER_BY_ID = "getTeacherById";
    
    Properties teacherQueries;
    JdbcTemplate jdbcTemplate;
    
    public JdbcTeacherDao(Properties teacherQueries, JdbcTemplate jdbcTemplate) {
        this.teacherQueries = teacherQueries;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TeacherEntity getTeacherById(int id) {
        return jdbcTemplate.query(teacherQueries.getProperty(GET_TEACHER_BY_ID), 
                preparedStatement -> preparedStatement.setInt(1, id),
                resultSet -> {
                    TeacherEntity teacher = new TeacherEntity();
                    while(resultSet.next()) {
                        teacher.setId(resultSet.getInt(TEACHER_ID));
                        teacher.setFirstName(resultSet.getString(FIRST_NAME));
                        teacher.setLastName(resultSet.getString(LAST_NAME));
                    }
                    return teacher;
                });
    }
}
