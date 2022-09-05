package ua.com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;

@Component
public final class CourseMapper implements RowMapper<CourseEntity> {
    
    private static final String TEACHER_ID = "teacher_id";
    private static final String NAME = "course_name";
    private static final String DESCRIPTION = "course_description";
    private static final String ID = "course_id";
    
    @Override
    public CourseEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        CourseEntity course = new CourseEntity();
        course.setId(resultSet.getInt(ID));
        course.setDescription(resultSet.getString(DESCRIPTION));
        course.setName(resultSet.getString(NAME));
        course.setTeacher(new TeacherEntity(resultSet.getInt(TEACHER_ID)));
        return course;
    }
}
