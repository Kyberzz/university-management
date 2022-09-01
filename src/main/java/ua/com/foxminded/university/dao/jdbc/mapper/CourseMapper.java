package ua.com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.jdbc.core.RowMapper;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

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
        
        
      //  TimetableMapper timetableMapper = new TimetableMapper();
      //  TimetableEntity timetable = timetableMapper.mapRow(resultSet, rowNum);
        
      //  course.getTimetableList().add(timetable);    
        return course;
    }
}
