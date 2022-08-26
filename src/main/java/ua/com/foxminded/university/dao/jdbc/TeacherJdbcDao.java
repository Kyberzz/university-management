package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

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
    private static final String GET_TEACHER_BY_ID = "teacher.getTeacherById";
    
    JdbcTemplate jdbcTemplate;
    Environment teacherQueries;
    
    @Autowired
    public TeacherJdbcDao(JdbcTemplate jdbcTemplate, Environment teacherQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherQueries = teacherQueries;
    }

    @Override
    public TeacherEntity getCourseListByTeacherId(int id) {
       return jdbcTemplate.query(teacherQueries.getProperty(GET_COURSE_LIST_BY_TEACHER_ID),
                                 preparedStatement -> preparedStatement.setInt(1, id), 
                                 resultSet -> {
                                     List<CourseEntity> courseList = new ArrayList<>();
                                     CourseEntity course = new CourseEntity();
                                     TeacherEntity teacher = new TeacherEntity();
                                     
                                     while(resultSet.next()) {
                                         if (resultSet.getInt(TEACHER_ID) != teacher.getId()) {
                                             teacher.setId(resultSet.getInt(TEACHER_ID));
                                             teacher.setFirstName(resultSet.getString(FIRST_NAME));
                                             teacher.setLastName(resultSet.getString(LAST_NAME));
                                         }
                                         
                                         course.setId(resultSet.getInt(COURSE_ID));
                                         course.setName(resultSet.getString(COURSE_NAME));
                                         course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                                         course.setTeacher(teacher);
                                         courseList.add(course);
                                         teacher.setCourseList(courseList);
                                     }
                                     return teacher;
                                 });
    }
    
    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(teacherQueries.getProperty(DELETE_BY_ID), 
                                   preparedStatement -> preparedStatement.setInt(1, id));
    }
    
    @Override
    public int update(TeacherEntity entity) {
        return jdbcTemplate.update(teacherQueries.getProperty(UPDATE), 
                                   preparedStatement -> {
                                       preparedStatement.setString(1, entity.getFirstName());
                                       preparedStatement.setString(2, entity.getLastName());
                                       preparedStatement.setInt(3, entity.getId());
                                   });
    }
    
    @Override
    public TeacherEntity getById(int id) {
        return jdbcTemplate.query(teacherQueries.getProperty(GET_TEACHER_BY_ID),
                                  preparedStatement -> preparedStatement.setInt(1, id), 
                                  resultSet -> {
                                      TeacherEntity teacher = new TeacherEntity();

                                      while (resultSet.next()) {
                                          teacher.setId(resultSet.getInt(TEACHER_ID));
                                          teacher.setFirstName(resultSet.getString(FIRST_NAME));
                                          teacher.setLastName(resultSet.getString(LAST_NAME));
                                      }
                                      return teacher;
                                  });
    }
    
    @Override
    public int insert(TeacherEntity entity) {
        return jdbcTemplate.update(teacherQueries.getProperty(INSERT), 
                                   preparedStatement -> {
                                       preparedStatement.setString(1, entity.getFirstName());
                                       preparedStatement.setString(2, entity.getLastName());
                                   });
    }
}
