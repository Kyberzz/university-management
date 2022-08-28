package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.TeacherEntity;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = JdbcDaoTestConfig.class)
@ExtendWith(SpringExtension.class)
class TeacherJdbcDaoTest {
    
    private static final String TEACHER_LAST_NAME_COLUMN = "last_name";
    private static final String TEACHER_FIRST_NAME_COLUMN = "first_name";
    private static final String TEACHER_ID_COLUMN = "id";
    private static final String SELECT_TEACHER_BY_ID = "test.selectTeacherById";
    private static final String COURSE_DESCRIPTION = "some description";
    private static final String COURSE_NAME = "Physics";
    private static final String TEACHER_LAST_NAME = "Einstein";
    private static final String TEACHER_FIRST_NAME = "Albert";
    private static final int NEW_TEACHER_ID = 3;
    private static final int TEACHER_ID = 2;
    private static final int COURSE_ID_NUMBER = 1;
    private static final int FIST_ELEMENT = 0;
    private static final int TEACHER_ID_NUMBER = 1;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private Environment teacherQueries;

    void insert_InsertingTeacherDatabaseData_DatabaseHasCorrectData() {
        TeacherEntity teacher = new TeacherEntity();
        teacher.setFirstName(TEACHER_FIRST_NAME);
        teacher.setLastName(TEACHER_LAST_NAME);
        TeacherDao teacherDao = new TeacherJdbcDao(jdbcTemplate, teacherQueries);
        teacherDao.insert(teacher);
        
        Map<String, Object> insertedTeacher = jdbcTemplate.queryForMap(SELECT_TEACHER_BY_ID, 
                                                                       NEW_TEACHER_ID);
        
        assertEquals(TEACHER_FIRST_NAME, insertedTeacher.get(TEACHER_FIRST_NAME_COLUMN));
        assertEquals(TEACHER_LAST_NAME, insertedTeacher.get(TEACHER_LAST_NAME_COLUMN));
    }
    
    @Test
    void getById_ReceivingTeacherDatabaseData_CorrectReceivedData() {
        TeacherDao teacherDao = new TeacherJdbcDao(jdbcTemplate, teacherQueries);
        TeacherEntity teacher = teacherDao.getById(TEACHER_ID_NUMBER);
        
        assertEquals(TEACHER_FIRST_NAME, teacher.getFirstName());
        assertEquals(TEACHER_LAST_NAME, teacher.getLastName());
        assertEquals(TEACHER_ID_NUMBER, teacher.getId());
    }
    
    @Test
    void update_UpdatingTeacherDatabaseData_DatabaseHasCorrectData() {
        TeacherDao teacherDao = new TeacherJdbcDao(jdbcTemplate, teacherQueries);
        TeacherEntity teacherData = new TeacherEntity();
        teacherData.setFirstName(TEACHER_FIRST_NAME);
        teacherData.setLastName(TEACHER_LAST_NAME);
        teacherData.setId(TEACHER_ID);
        teacherDao.update(teacherData);
        Map<String, Object> updatedTeacherData = jdbcTemplate.queryForMap(
                teacherQueries.getProperty(SELECT_TEACHER_BY_ID), 
                TEACHER_ID);
        
        assertEquals(TEACHER_ID, updatedTeacherData.get(TEACHER_ID_COLUMN));
        assertEquals(TEACHER_FIRST_NAME, updatedTeacherData.get(TEACHER_FIRST_NAME_COLUMN));
        assertEquals(TEACHER_LAST_NAME, updatedTeacherData.get(TEACHER_LAST_NAME_COLUMN));
    }
    
    @Test
    void deleteById_DeletingTeacherDatabaseData_DatabaseHasNoData() {
        TeacherDao teacherDao = new TeacherJdbcDao(jdbcTemplate, teacherQueries);
        teacherDao.deleteById(TEACHER_ID_NUMBER);
        jdbcTemplate.query(teacherQueries.getProperty(SELECT_TEACHER_BY_ID), 
                           preparedStatement -> preparedStatement.setInt(1, TEACHER_ID_NUMBER), 
                           resultSet -> {
                               assertTrue(!resultSet.next());
                           });  
    }
    
    @Test
    void getCourseListByTeacherId_ReceivingTeacherDatabaseData_CorrectReceivedData() {
        TeacherDao teacherDao = new TeacherJdbcDao(jdbcTemplate, teacherQueries);
        TeacherEntity teacherData = teacherDao.getCourseListByTeacherId(TEACHER_ID_NUMBER);
        
        assertEquals(TEACHER_ID_NUMBER, teacherData.getId());
        assertEquals(TEACHER_FIRST_NAME, teacherData.getFirstName());
        assertEquals(TEACHER_LAST_NAME, teacherData.getLastName());
        assertEquals(TEACHER_ID_NUMBER, teacherData.getCourseList().get(FIST_ELEMENT).getTeacher().getId());
        assertEquals(COURSE_ID_NUMBER, teacherData.getCourseList().get(FIST_ELEMENT).getId());
        assertEquals(COURSE_NAME, teacherData.getCourseList().get(FIST_ELEMENT).getName());
        assertEquals(COURSE_DESCRIPTION, teacherData.getCourseList().get(FIST_ELEMENT).getDescription());
    }
}
