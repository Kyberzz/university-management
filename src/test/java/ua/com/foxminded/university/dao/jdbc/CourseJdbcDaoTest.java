package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;

@ContextConfiguration(classes = JdbcDaoTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class CourseJdbcDaoTest {
    
    private static final String MONDAY = "MONDAY";
    private static final String EXPECTED_COURSE_NAME = "Programming";
    private static final String DATABASE_COURSE_NAME = "Physics";
    private static final String TEACHER_ID_COLUMN = "teacher_id";
    private static final String COURSE_DESCRIPTION_COLUMN = "description";
    private static final String COURSE_NAME_COLUMN = "name";
    private static final String COURSE_ID_COLUMN = "id";
    private static final String COURSE_DESCRIPTION = "some description";
    private static final String COURSE_NAME = "Chemistry";
    private static final String SELECT_BY_ID = "selectById";
    private static final long END_TIME = 39360000;
    private static final long START_TIME = 36360000;
    private static final int EXPECTED_COURSE_ID = 2;
    private static final int GROUP_ID_NUMBER = 1;
    private static final int TIMETABLE_ID = 2;
    private static final int DATABASE_COURSE_ID = 4;
    private static final int DATABASE_TEACHER_ID = 1;
    private static final int TEACHER_ID_NUMBER = 2;
    private static final int COURSE_ID = 1;
    
    
    @Autowired
    public JdbcTemplate jdbcTemplate;
    
    @Autowired
    public Properties courseQueries;
    
    @Autowired
    public Properties testQueries;
    
    void getTimetableListByCourseId_GettingDatabaseTimetableData_CorrectData() {
        CourseJdbcDao courseDao = new CourseJdbcDao(courseQueries, jdbcTemplate);
        CourseEntity receivedCourse = courseDao.getTimetableListByCourseId(COURSE_ID);
        CourseEntity expectedResult = new CourseEntity();
        expectedResult.setId(COURSE_ID);
        expectedResult.setName(DATABASE_COURSE_NAME);
        expectedResult.setTeacher(new TeacherEntity(DATABASE_TEACHER_ID));
        List<TimetableEntity> timetableList = new ArrayList<>();
        TimetableEntity timetable = new TimetableEntity();
        timetable.setId(TIMETABLE_ID);
        timetable.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        timetable.setCourse(new CourseEntity(GROUP_ID_NUMBER));
        timetable.setStartTime(START_TIME);
        timetable.setEndTime(END_TIME);
        timetable.setWeekDay(WeekDayEntity.valueOf(MONDAY));
        timetableList.add(timetable);
        expectedResult.setId(EXPECTED_COURSE_ID);
        expectedResult.setName(EXPECTED_COURSE_NAME);
        expectedResult.setTeacher(new TeacherEntity(TEACHER_ID_NUMBER));
        assertEquals(expectedResult, receivedCourse);
    }
    
    void insert_InsertingCourseDataToDatabase_DatabaseHasCorrectData() {
        CourseJdbcDao courseDao = new CourseJdbcDao(courseQueries, jdbcTemplate);
        CourseEntity course = new CourseEntity();
        course.setName(COURSE_NAME);
        course.setDescription(COURSE_DESCRIPTION);
        course.setTeacher(new TeacherEntity(TEACHER_ID_NUMBER));
        courseDao.insert(course);
        Map<String, Object> insertedCourse = jdbcTemplate.queryForMap(
                testQueries.getProperty(SELECT_BY_ID), 
                DATABASE_COURSE_ID);
        assertEquals(course.getId(), insertedCourse.get(COURSE_ID_COLUMN));
        assertEquals(course.getName(), insertedCourse.get(COURSE_NAME_COLUMN));
        assertEquals(course.getDescription(), insertedCourse.get(COURSE_DESCRIPTION_COLUMN));
        assertEquals(course.getTeacher().getId(), insertedCourse.get(TEACHER_ID_COLUMN));
    }
    
    void getById_GettingCourseDatabaseData_CorrectData() {
        CourseJdbcDao courseDao = new CourseJdbcDao(courseQueries, jdbcTemplate);
        CourseEntity course = courseDao.getById(COURSE_ID);
        CourseEntity expectedResult = new CourseEntity();
        expectedResult.setId(COURSE_ID);
        expectedResult.setName(DATABASE_COURSE_NAME);
        expectedResult.setTeacher(new TeacherEntity(DATABASE_TEACHER_ID));
        assertEquals(expectedResult, course);
    }
    
    @Test
    void update_UpdatingDatabaseCourseData_DatabaseHasCorrectData() {
        CourseJdbcDao courseDao = new CourseJdbcDao(courseQueries, jdbcTemplate);
        CourseEntity course = new CourseEntity();
        course.setId(COURSE_ID);
        course.setName(COURSE_NAME);
        course.setDescription(COURSE_DESCRIPTION);
        course.setTeacher(new TeacherEntity(TEACHER_ID_NUMBER));
        courseDao.update(course);
        Map<String, Object> databaseCourse = jdbcTemplate.queryForMap(
                testQueries.getProperty(SELECT_BY_ID), 
                COURSE_ID);
        assertEquals(course.getId(), databaseCourse.get(COURSE_ID_COLUMN));
        assertEquals(course.getName(), databaseCourse.get(COURSE_NAME_COLUMN));
        assertEquals(course.getDescription(), databaseCourse.get(COURSE_DESCRIPTION_COLUMN));
        assertEquals(course.getTeacher().getId(), databaseCourse.get(TEACHER_ID_COLUMN));
    }
    
    @Test
    void deleteById_DeletingDatabaseCourseData_DatabaseHasNoCourseData() {
        CourseJdbcDao courseDao = new CourseJdbcDao(courseQueries, jdbcTemplate);
        courseDao.deleteById(COURSE_ID);
        jdbcTemplate.query(testQueries.getProperty(SELECT_BY_ID),
                           preparedStatement -> preparedStatement.setInt(1, COURSE_ID), 
                           resultSet -> {
                               assertTrue(!resultSet.next());
                           });
    }
}
