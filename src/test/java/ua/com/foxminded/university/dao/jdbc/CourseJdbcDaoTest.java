package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

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

import ua.com.foxminded.university.config.TestAppConfig;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TimetableMapper;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;

@ContextConfiguration(classes = TestAppConfig.class)
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
    private static final int TIMETABLES_QUANTITY = 2;
    private static final int COURSE_ID_NUMBER = 2;
    private static final int EXPECTED_GROUP_ID = 1;
    private static final int EXPECTED_TIMETABLE_ID = 2;
    private static final int EXPECTED_COURSE_ID = 4;
    private static final int EXPECTED_TEACHER_ID = 2;
    private static final int FIRST_ELEMENT = 0;
    private static final int NO_ID = 0;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private  Environment queries;
    
    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private TimetableMapper timetableMapper;
    
    
    @Test
    void getTimetableListByCourseId_GettingDatabaseTimetableData_CorrectData() throws DaoException {
        CourseJdbcDao courseDao = new CourseJdbcDao(jdbcTemplate, queries, courseMapper, timetableMapper);
        CourseEntity receivedCourse = courseDao.getTimetableListByCourseId(COURSE_ID_NUMBER);
        
        assertEquals(COURSE_ID_NUMBER, receivedCourse.getId());
        assertEquals(EXPECTED_COURSE_NAME, receivedCourse.getName());
        assertEquals(EXPECTED_TEACHER_ID, receivedCourse.getTeacher().getId());
        assertEquals(COURSE_ID_NUMBER, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getCourse()
                                                                                           .getId());
        assertEquals(EXPECTED_START_TIME, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getStartTime());
        assertEquals(EXPECTED_END_TIME, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getEndTime());
        assertEquals(EXPECTED_GROUP_ID, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getGroup()
                                                                                            .getId());
        assertEquals(EXPECTED_TIMETABLE_ID, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getId());
        assertEquals(MONDAY, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getWeekDay().toString());
        assertEquals(TIMETABLES_QUANTITY, receivedCourse.getTimetableList().size());
    }
    
    @Test
    void insert_InsertingCourseDataToDatabase_DatabaseHasCorrectData() throws DaoException {
        CourseJdbcDao courseDao = new CourseJdbcDao(jdbcTemplate, queries, courseMapper, timetableMapper);
        CourseEntity course = new CourseEntity(NO_ID);
        course.setName(NEW_COURSE_NAME);
        course.setTeacher(new TeacherEntity(EXPECTED_TEACHER_ID));
        course.setDescription(EXPECTED_COURSE_DESCRIPTION);
        courseDao.insert(course);
        String sqlSelectCourseById = queries.getProperty(SELECT_COURSE_BY_ID);
        
        Map<String, Object> insertedCourse = jdbcTemplate.queryForMap(sqlSelectCourseById, 
                                                                      EXPECTED_COURSE_ID);
       
        assertEquals(EXPECTED_COURSE_ID, insertedCourse.get(COURSE_ID_COLUMN));
        assertEquals(NEW_COURSE_NAME, insertedCourse.get(COURSE_NAME_COLUMN));
        assertEquals(EXPECTED_COURSE_DESCRIPTION, insertedCourse.get(COURSE_DESCRIPTION_COLUMN));
        assertEquals(EXPECTED_TEACHER_ID, insertedCourse.get(TEACHER_ID_COLUMN));
    }
    
    @Test
    void getById_GettingDatabaseCourseData_CorrectData() throws DaoException {
        CourseJdbcDao courseDao = new CourseJdbcDao(jdbcTemplate, queries, courseMapper, timetableMapper);
        CourseEntity receivedCourse = courseDao.getById(COURSE_ID_NUMBER);
        
        assertEquals(COURSE_ID_NUMBER, receivedCourse.getId());
        assertEquals(EXPECTED_COURSE_NAME, receivedCourse.getName());
        assertEquals(EXPECTED_TEACHER_ID, receivedCourse.getTeacher().getId());
        assertEquals(EXPECTED_COURSE_DESCRIPTION, receivedCourse.getDescription());
    }
    
    @Test
    void update_UdatingDatabaseWithNullValues_DatabaseHasNoData() throws DaoException {
        CourseJdbcDao courseDao = new CourseJdbcDao(jdbcTemplate, queries, courseMapper, timetableMapper);
        CourseEntity course = new CourseEntity(COURSE_ID_NUMBER);
        course.setName(EXPECTED_COURSE_NAME);
        course.setTeacher(new TeacherEntity(NO_ID));
        courseDao.update(course);
        String sqlSelectCourseById = queries.getProperty(SELECT_COURSE_BY_ID);
        Map<String, Object> updatedCourse = jdbcTemplate.queryForMap(sqlSelectCourseById, 
                                                                     COURSE_ID_NUMBER);
        assertNull(updatedCourse.get(COURSE_DESCRIPTION_COLUMN));
        assertNull(updatedCourse.get(TEACHER_ID_COLUMN));
    }
    
    @Test
    void update_UpdatingDatabaseCourseData_DatabaseHasCorrectData() throws DaoException {
        CourseJdbcDao courseDao = new CourseJdbcDao(jdbcTemplate, queries, courseMapper, timetableMapper);
        CourseEntity course = new CourseEntity(COURSE_ID_NUMBER);
        course.setName(NEW_COURSE_NAME);
        course.setDescription(EXPECTED_COURSE_DESCRIPTION);
        course.setTeacher(new TeacherEntity(EXPECTED_TEACHER_ID));
        courseDao.update(course);
        String sqlSelectCourseById = queries.getProperty(SELECT_COURSE_BY_ID);
        Map<String, Object> databaseCourse = jdbcTemplate.queryForMap(sqlSelectCourseById, 
                                                                      COURSE_ID_NUMBER);
        assertEquals(COURSE_ID_NUMBER, databaseCourse.get(COURSE_ID_COLUMN));
        assertEquals(NEW_COURSE_NAME, databaseCourse.get(COURSE_NAME_COLUMN));
        assertEquals(EXPECTED_COURSE_DESCRIPTION, databaseCourse.get(COURSE_DESCRIPTION_COLUMN));
        assertEquals(EXPECTED_TEACHER_ID, databaseCourse.get(TEACHER_ID_COLUMN));
    }
    
    @Test
    void deleteById_DeletingDatabaseCourseData_DatabaseHasNoCourseData() throws DaoException {
        CourseJdbcDao courseDao = new CourseJdbcDao(jdbcTemplate, queries, courseMapper, timetableMapper);
        courseDao.deleteById(COURSE_ID_NUMBER);
        String sqlSelectCourseById = queries.getProperty(SELECT_COURSE_BY_ID);
        jdbcTemplate.query(sqlSelectCourseById,
                           preparedStatement -> preparedStatement.setInt(1, COURSE_ID_NUMBER), 
                           resultSet -> {
                               assertTrue(!resultSet.next());
                           });
    }
}
