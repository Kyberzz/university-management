package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;

@ContextConfiguration(classes = JdbcDaoTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class CourseJdbcDaoTest {
    
    private static final String EXPECTED_COURSE_NAME = "Programming";
    private static final String EXPECTED_COURSE_DESCRIPTION = "some description";
    private static final String TEACHER_ID_COLUMN = "teacher_id";
    private static final String COURSE_DESCRIPTION_COLUMN = "description";
    private static final String COURSE_NAME_COLUMN = "name";
    private static final String COURSE_ID_COLUMN = "id";
    private static final String NEW_COURSE_NAME = "Chemistry";
    private static final String SELECT_COURSE_BY_ID = "test.selectCourseById";
    private static final String MONDAY = "MONDAY";
    private static final long EXPECTED_END_TIME = 39360000;
    private static final long EXPECTED_START_TIME = 36360000;
    private static final int COURSE_ID_NUMBER = 2;
    private static final int EXPECTED_GROUP_ID = 1;
    private static final int EXPECTED_TIMETABLE_ID = 2;
    private static final int EXPECTED_COURSE_ID = 4;
    private static final int EXPECTED_TEACHER_ID = 2;
    private static final int FIRST_ELEMENT = 0;
    
    @Autowired
    public JdbcTemplate jdbcTemplate;
    
    @Autowired
    public Environment queries;
    
    @Test
    void getTimetableListByCourseId_GettingDatabaseTimetableData_CorrectData() {
        CourseJdbcDao courseDao = new CourseJdbcDao(jdbcTemplate, queries);
        CourseEntity receivedCourse = courseDao.getTimetableListByCourseId(COURSE_ID_NUMBER);
        
        assertEquals(COURSE_ID_NUMBER, receivedCourse.getId());
        assertEquals(EXPECTED_COURSE_NAME, receivedCourse.getName());
        assertEquals(EXPECTED_TEACHER_ID, receivedCourse.getTeacher()
                                                        .getId());
        assertEquals(COURSE_ID_NUMBER, receivedCourse.getTimetableList()
                                                     .get(FIRST_ELEMENT)
                                                     .getCourse()
                                                     .getId());
        assertEquals(EXPECTED_START_TIME, receivedCourse.getTimetableList()
                                                        .get(FIRST_ELEMENT)
                                                        .getStartTime());
        assertEquals(EXPECTED_END_TIME, receivedCourse.getTimetableList()
                                                      .get(FIRST_ELEMENT)
                                                      .getEndTime());
        assertEquals(EXPECTED_GROUP_ID, receivedCourse.getTimetableList()
                                                      .get(FIRST_ELEMENT)
                                                      .getGroup()
                                                      .getId());
        assertEquals(EXPECTED_TIMETABLE_ID, receivedCourse.getTimetableList()
                                                          .get(FIRST_ELEMENT)
                                                          .getId());
        assertEquals(MONDAY, receivedCourse.getTimetableList()
                                           .get(FIRST_ELEMENT)
                                           .getWeekDay()
                                           .toString());
        
    }
    
    @Test
    void insert_InsertingCourseDataToDatabase_DatabaseHasCorrectData() {
        CourseJdbcDao courseDao = new CourseJdbcDao(jdbcTemplate, queries);
        CourseEntity course = new CourseEntity();
        course.setName(NEW_COURSE_NAME);
        course.setTeacher(new TeacherEntity(EXPECTED_TEACHER_ID));
        course.setDescription(EXPECTED_COURSE_DESCRIPTION);
        courseDao.insert(course);
        Map<String, Object> insertedCourse = jdbcTemplate.queryForMap(queries.getProperty(SELECT_COURSE_BY_ID), 
                                                                      EXPECTED_COURSE_ID);
       
        assertEquals(EXPECTED_COURSE_ID, insertedCourse.get(COURSE_ID_COLUMN));
        assertEquals(NEW_COURSE_NAME, insertedCourse.get(COURSE_NAME_COLUMN));
        assertEquals(EXPECTED_COURSE_DESCRIPTION, insertedCourse.get(COURSE_DESCRIPTION_COLUMN));
        assertEquals(EXPECTED_TEACHER_ID, insertedCourse.get(TEACHER_ID_COLUMN));
    }
    
    @Test
    void getById_GettingDatabaseCourseData_CorrectData() {
        CourseJdbcDao courseDao = new CourseJdbcDao(jdbcTemplate, queries);
        CourseEntity receivedCourse = courseDao.getById(COURSE_ID_NUMBER);
        
        assertEquals(COURSE_ID_NUMBER, receivedCourse.getId());
        assertEquals(EXPECTED_COURSE_NAME, receivedCourse.getName());
        assertEquals(EXPECTED_TEACHER_ID, receivedCourse.getTeacher().getId());
        assertEquals(EXPECTED_COURSE_DESCRIPTION, receivedCourse.getDescription());
    }
    
    @Test
    void update_UpdatingDatabaseCourseData_DatabaseHasCorrectData() {
        CourseJdbcDao courseDao = new CourseJdbcDao(jdbcTemplate, queries);
        CourseEntity course = new CourseEntity();
        course.setId(COURSE_ID_NUMBER);
        course.setName(NEW_COURSE_NAME);
        course.setDescription(EXPECTED_COURSE_DESCRIPTION);
        course.setTeacher(new TeacherEntity(EXPECTED_TEACHER_ID));
        courseDao.update(course);
        Map<String, Object> databaseCourse = jdbcTemplate.queryForMap(
                queries.getProperty(SELECT_COURSE_BY_ID), 
                COURSE_ID_NUMBER);
        assertEquals(COURSE_ID_NUMBER, databaseCourse.get(COURSE_ID_COLUMN));
        assertEquals(NEW_COURSE_NAME, databaseCourse.get(COURSE_NAME_COLUMN));
        assertEquals(EXPECTED_COURSE_DESCRIPTION, databaseCourse.get(COURSE_DESCRIPTION_COLUMN));
        assertEquals(EXPECTED_TEACHER_ID, databaseCourse.get(TEACHER_ID_COLUMN));
    }
    
    @Test
    void deleteById_DeletingDatabaseCourseData_DatabaseHasNoCourseData() {
        CourseJdbcDao courseDao = new CourseJdbcDao(jdbcTemplate, queries);
        courseDao.deleteById(COURSE_ID_NUMBER);
        jdbcTemplate.query(queries.getProperty(SELECT_COURSE_BY_ID),
                           preparedStatement -> preparedStatement.setInt(1, COURSE_ID_NUMBER), 
                           resultSet -> {
                               assertTrue(!resultSet.next());
                           });
    }
}
