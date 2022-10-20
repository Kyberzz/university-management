package ua.com.foxminded.university.dao.jdbc;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.config.AppConfigTest;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.repository.DaoException;
import ua.com.foxminded.university.repository.jdbc.CourseJdbcRepository;

@ContextConfiguration(classes = AppConfigTest.class)

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class CourseJdbcRepositoryTest {
    
    private static final String EXPECTED_COURSE_NAME = "Programming";
    private static final String NEW_COURSE_DESCRIPTION = "some description";
    private static final String NEW_COURSE_NAME = "Chemistry";
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
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    void getTimetableListByCourseId_GettingDatabaseTimetableData_CorrectData() throws DaoException {
        CourseJdbcRepository courseDao = new CourseJdbcRepository(entityManagerFactory);
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
        CourseJdbcRepository courseDao = new CourseJdbcRepository(entityManagerFactory);
        CourseEntity course = new CourseEntity();
        course.setName(NEW_COURSE_NAME);
        TeacherEntity teacher = new TeacherEntity();
        teacher.setId(EXPECTED_TEACHER_ID);
        course.setTeacher(teacher);
        course.setDescription(NEW_COURSE_DESCRIPTION);
        courseDao.insert(course);
        
        CourseEntity insertedCourse = entityManagerFactory.createEntityManager()
                                                          .find(CourseEntity.class, EXPECTED_COURSE_ID);
        assertEquals(EXPECTED_COURSE_ID, insertedCourse.getId());
        assertEquals(NEW_COURSE_NAME, insertedCourse.getName());
        assertEquals(NEW_COURSE_DESCRIPTION, insertedCourse.getDescription());
        assertEquals(EXPECTED_TEACHER_ID, insertedCourse.getTeacher().getId());
    }
    
    @Test
    void getById_GettingDatabaseCourseData_CorrectData() throws DaoException {
        CourseJdbcRepository courseDao = new CourseJdbcRepository(entityManagerFactory);
        CourseEntity receivedCourse = courseDao.getById(COURSE_ID_NUMBER);
        
        assertEquals(COURSE_ID_NUMBER, receivedCourse.getId());
        assertEquals(EXPECTED_COURSE_NAME, receivedCourse.getName());
        assertEquals(EXPECTED_TEACHER_ID, receivedCourse.getTeacher().getId());
        assertEquals(NEW_COURSE_DESCRIPTION, receivedCourse.getDescription());
    }
    
    @Test
    void update_UdatingDatabaseWithNullValues_DatabaseHasNoData() throws DaoException {
        CourseJdbcRepository courseDao = new CourseJdbcRepository(entityManagerFactory);
        CourseEntity course = new CourseEntity();
        course.setId(COURSE_ID_NUMBER);
        course.setName(EXPECTED_COURSE_NAME);
        course.setTeacher(null);
        course.setDescription(null);
        courseDao.update(course);
        CourseEntity updatedCourse = entityManagerFactory.createEntityManager()
                                                         .find(CourseEntity.class, COURSE_ID_NUMBER);
        
        assertNull(updatedCourse.getDescription());
        assertNull(updatedCourse.getTeacher());
    }
    
    @Test
    void update_UpdatingDatabaseCourseData_DatabaseHasCorrectData() throws DaoException {
        CourseJdbcRepository courseDao = new CourseJdbcRepository(entityManagerFactory);
        CourseEntity course = new CourseEntity();
        course.setId(COURSE_ID_NUMBER);
        course.setName(NEW_COURSE_NAME);
        course.setDescription(NEW_COURSE_DESCRIPTION);
        TeacherEntity teacher = new TeacherEntity();
        teacher.setId(EXPECTED_TEACHER_ID);
        course.setTeacher(teacher);
        
        courseDao.update(course);
        
        CourseEntity databaseCourse = entityManagerFactory.createEntityManager()
                                                          .find(CourseEntity.class, 
                                                                COURSE_ID_NUMBER);
        assertEquals(COURSE_ID_NUMBER, databaseCourse.getId());
        assertEquals(NEW_COURSE_NAME, databaseCourse.getName());
        assertEquals(NEW_COURSE_DESCRIPTION, databaseCourse.getDescription());
        assertEquals(EXPECTED_TEACHER_ID, databaseCourse.getTeacher().getId());
    }
    
    @Test
    void deleteById_DeletingDatabaseCourseData_DatabaseHasNoCourseData() throws DaoException {
        CourseJdbcRepository courseDao = new CourseJdbcRepository(entityManagerFactory);
        courseDao.deleteById(COURSE_ID_NUMBER);
        CourseEntity course = new CourseEntity();
        course.setId(COURSE_ID_NUMBER);
        boolean existenceStatus = entityManagerFactory.createEntityManager()
                                                      .contains(course);
        assertFalse(existenceStatus);
    }
}
