package ua.com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

public final class StudentMapper implements RowMapper<StudentEntity> {
    
    private static final String STUDENT_ID = "student_id";
    private static final String GROUP_ID = "group_id";
    private static final String STUDENT_LAST_NAME = "student_last_name";
    private static final String STUEDNT_FIRST_NAME = "student_first_name";
    
    
    public StudentEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        StudentEntity student = new StudentEntity();
        student.setFirstName(resultSet.getString(STUEDNT_FIRST_NAME));
        student.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
        student.setId(resultSet.getInt(STUDENT_ID));
        student.setLastName(resultSet.getString(STUDENT_LAST_NAME));
        return student;
    }
}
