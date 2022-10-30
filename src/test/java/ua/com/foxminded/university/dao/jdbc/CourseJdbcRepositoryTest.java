package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.config.AppConfigTest;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.DayOfWeek;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.RepositoryException;

@Transactional
@ContextConfiguration(classes = AppConfigTest.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class CourseJdbcRepositoryTest {
    
    private static final String GROUP_NAME = "lk-89";
    private static final String WEEK_DAY = "THURSDAY";
    private static final String NEW_TEACHER_LAST_NAME = "Booch";
    private static final String TEACHER_LAST_NAME = "Fock";
    private static final String NEW_TEACHER_FIRST_NAME = "John";
    private static final String TEACHER_FIRST_NAME = "Stiven";
    private static final String COURSE_NAME = "Programming";
    private static final String TIMETABLE_DESCRIPTION = "some description";
    private static final String COURSE_DESCRIPTION = "some description";
    private static final String NEW_COURSE_DESCRIPTION = "new description";
    private static final String NEW_COURSE_NAME = "Chemistry";
    private static final String MONDAY = "MONDAY";
    private static final long END_TIME = 39360000;
    private static final long START_TIME = 36360000;
    private static final int TIMETABLES_QUANTITY = 2;
    private static final int COURSE_ID = 1;
    private static final int GROUP_ID = 1;
    private static final int EXPECTED_TIMETABLE_ID = 2;
    private static final int NEW_COURSE_ID = 2;
    private static final int NEW_TEACHER_ID = 2;
    private static final int TEACHER_ID = 1;
    private static final int FIRST_ELEMENT = 0;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @BeforeEach
    void init() {
        GroupEntity group = new GroupEntity();
        group.setName(GROUP_NAME);
        entityManager.persist(group);
        
        TeacherEntity teacher = new TeacherEntity();
        teacher.setFirstName(TEACHER_FIRST_NAME);
        teacher.setLastName(TEACHER_LAST_NAME);
        entityManager.persist(teacher);
        
        TeacherEntity secondTeacher = new TeacherEntity();
        secondTeacher.setFirstName(NEW_TEACHER_FIRST_NAME);
        secondTeacher.setLastName(NEW_TEACHER_LAST_NAME);
        
        CourseEntity course = new CourseEntity();
        course.setName(COURSE_NAME);
        course.setDescription(COURSE_DESCRIPTION);
        course.setTeacher(teacher);
        entityManager.persist(course);
        
        TimetableEntity timetable = new TimetableEntity();
        timetable.setCourse(course);
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setEndTime(END_TIME);
        timetable.setStartTime(START_TIME);
        timetable.setWeekDay(DayOfWeek.valueOf(WEEK_DAY));
        entityManager.persist(timetable);
    }
    
    @Test
    void getById_GettingDatabaseCourseData_CorrectData() throws RepositoryException {
        CourseEntity receivedCourse = courseRepository.getById(COURSE_ID);
        
        assertEquals(COURSE_ID, receivedCourse.getId());
        assertEquals(COURSE_NAME, receivedCourse.getName());
        assertEquals(TEACHER_ID, receivedCourse.getTeacher().getId());
        assertEquals(COURSE_DESCRIPTION, receivedCourse.getDescription());
    }

    @Test
    void update_UpdatingDatabaseCourseData_DatabaseHasCorrectData() throws RepositoryException {
        CourseEntity course = new CourseEntity();
        course.setId(COURSE_ID);
        course.setName(NEW_COURSE_NAME);
        course.setDescription(NEW_COURSE_DESCRIPTION);
        TeacherEntity teacher = new TeacherEntity();
        teacher.setId(NEW_TEACHER_ID);
        course.setTeacher(teacher);
        
        courseRepository.update(course);
        
        CourseEntity databaseCourse = entityManager.find(CourseEntity.class, COURSE_ID);
        assertEquals(COURSE_ID, databaseCourse.getId());
        assertEquals(NEW_COURSE_NAME, databaseCourse.getName());
        assertEquals(NEW_COURSE_DESCRIPTION, databaseCourse.getDescription());
        assertEquals(NEW_TEACHER_ID, databaseCourse.getTeacher().getId());
    }
        
    @Test
    void update_UdatingDatabaseWithNullValues_DatabaseHasNoData() throws RepositoryException {
        CourseEntity course = new CourseEntity();
        course.setId(COURSE_ID);
        course.setName(NEW_COURSE_NAME);
        course.setTeacher(null);
        course.setDescription(null);
        
        courseRepository.update(course);
        
        CourseEntity updatedCourse = entityManager.find(CourseEntity.class, COURSE_ID);
        
        assertNull(updatedCourse.getDescription());
        assertNull(updatedCourse.getTeacher());
    }
    
    @Test
    void deleteById_DeletingDatabaseCourseData_DatabaseHasNoCourseData() throws RepositoryException {
        courseRepository.deleteById(COURSE_ID);
        CourseEntity course = new CourseEntity();
        course.setId(COURSE_ID);
        boolean existenceStatus = entityManager.contains(course);
        assertFalse(existenceStatus);
    }
    
    @Test
    void insert_InsertingCourseDataToDatabase_DatabaseHasCorrectData() throws RepositoryException {
        TeacherEntity teacher = new TeacherEntity();
        teacher.setId(TEACHER_ID);

        CourseEntity course = new CourseEntity();
        course.setTeacher(teacher);
        course.setName(NEW_COURSE_NAME);
        course.setDescription(NEW_COURSE_DESCRIPTION);

        courseRepository.insert(course);
        
        CourseEntity insertedCourse = entityManager.find(CourseEntity.class, NEW_COURSE_ID);
        assertEquals(NEW_COURSE_ID, insertedCourse.getId());
        assertEquals(NEW_COURSE_NAME, insertedCourse.getName());
        assertEquals(NEW_COURSE_DESCRIPTION, insertedCourse.getDescription());
        assertEquals(TEACHER_ID, insertedCourse.getTeacher().getId());
    }
    
    @Test
    void getTimetableListByCourseId_GettingDatabaseTimetableData_CorrectData() throws RepositoryException {
        CourseEntity receivedCourse = courseRepository.getTimetableListByCourseId(COURSE_ID);
        
        assertEquals(COURSE_ID, receivedCourse.getId());
        assertEquals(COURSE_NAME, receivedCourse.getName());
        assertEquals(TEACHER_ID, receivedCourse.getTeacher().getId());
        assertEquals(COURSE_ID, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getCourse()
                                                                                           .getId());
        assertEquals(START_TIME, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getStartTime());
        assertEquals(END_TIME, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getEndTime());
        assertEquals(GROUP_ID, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getGroup()
                                                                                            .getId());
        assertEquals(EXPECTED_TIMETABLE_ID, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getId());
        assertEquals(MONDAY, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getWeekDay().toString());
        assertEquals(TIMETABLES_QUANTITY, receivedCourse.getTimetableList().size());
    }
}
