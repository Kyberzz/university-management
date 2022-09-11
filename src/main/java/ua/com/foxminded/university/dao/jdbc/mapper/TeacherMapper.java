package ua.com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.entity.TeacherEntity;

@Component
public final class TeacherMapper implements RowMapper<TeacherEntity> {
    
    private static final String TEACHER_ID = "teacher_id";
    private static final String TEACHER_FIRST_NAME = "teacher_first_name";
    private static final String TEACHER_LAST_NAME = "teacher_last_name";
    
    @Override
    public TeacherEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        TeacherEntity teacher = new TeacherEntity();
        teacher.setFirstName(resultSet.getString(TEACHER_FIRST_NAME));
        teacher.setId(resultSet.getInt(TEACHER_ID));
        teacher.setLastName(resultSet.getString(TEACHER_LAST_NAME));
        return teacher;
    }
}
