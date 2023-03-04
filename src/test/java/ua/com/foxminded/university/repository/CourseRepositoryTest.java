package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.config.RepositoryTestConfig;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.exception.RepositoryException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryTestConfig.class)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CourseRepositoryTest {
    
    private static final String GROUP_NAME = "lk-89";
    private static final String WEEK_DAY = "THURSDAY";
    private static final String TEACHER_LAST_NAME = "Fock";
    private static final String TEACHER_FIRST_NAME = "Stiven";
    private static final String COURSE_NAME = "Programming";
    private static final String TIMETABLE_DESCRIPTION = "some description";
    private static final String COURSE_DESCRIPTION = "some description";
    private static final int MINUTE = 0;
    private static final int END_TIME = 9;
    private static final int START_TIME = 8;
    private static final int COURSE_ID = 1;
    private static final int GROUP_ID = 1;
    private static final int TIMETABLE_ID = 1;
    private static final int TEACHER_ID = 1;
    private static final int FIRST_ELEMENT = 0;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @BeforeEach
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
       
        entityManager.getTransaction().begin();
        GroupEntity group = new GroupEntity();
        group.setName(GROUP_NAME);
        entityManager.persist(group);
        
        TeacherEntity teacher = new TeacherEntity();
        teacher.setUser(new UserEntity());
        teacher.getUser().setFirstName(TEACHER_FIRST_NAME);
        teacher.getUser().setLastName(TEACHER_LAST_NAME);
        entityManager.persist(teacher);
        entityManager.flush();
        
        CourseEntity course = new CourseEntity();
        course.setName(COURSE_NAME);
        course.setDescription(COURSE_DESCRIPTION);
        course.setTeacher(teacher);
        entityManager.persist(course);
        entityManager.flush();
        
        TimetableEntity timetable = new TimetableEntity();
        timetable.setGroup(group);
        timetable.setCourse(course);
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setEndTime(LocalTime.of(END_TIME, MINUTE));
        timetable.setStartTime(LocalTime.of(START_TIME, MINUTE));
        timetable.setDayOfWeek(DayOfWeek.valueOf(WEEK_DAY));
        entityManager.persist(timetable);
        entityManager.getTransaction().commit();
    }

    @Test
    void findTimetableListById_GettingDatabaseTimetableData_CorrectData() throws RepositoryException {
        CourseEntity receivedCourse = courseRepository.findTimetableListById(COURSE_ID);
        
        assertEquals(COURSE_ID, receivedCourse.getId());
        assertEquals(COURSE_NAME, receivedCourse.getName());
        assertEquals(TEACHER_ID, receivedCourse.getTeacher().getId());
        assertEquals(COURSE_ID, receivedCourse.getTimetableList().get(FIRST_ELEMENT)
                                                                 .getCourse()
                                                                 .getId());
        assertEquals(LocalTime.of(START_TIME, MINUTE), 
                     receivedCourse.getTimetableList().get(FIRST_ELEMENT).getStartTime());
        assertEquals(LocalTime.of(END_TIME, MINUTE), 
                     receivedCourse.getTimetableList().get(FIRST_ELEMENT).getEndTime());
        assertEquals(GROUP_ID, receivedCourse.getTimetableList().get(FIRST_ELEMENT)
                                                                .getGroup()
                                                                .getId());
        assertEquals(TIMETABLE_ID, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getId());
        assertEquals(WEEK_DAY, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getDayOfWeek()
                                                                                   .toString());
    }
    
    @Test
    void findById_GettingCourseById_CorrectRetrievedData() throws RepositoryException {
        CourseEntity course = courseRepository.findById(COURSE_ID);
        assertEquals(COURSE_ID, course.getId());
        assertEquals(COURSE_DESCRIPTION, course.getDescription());
        assertEquals(COURSE_NAME, course.getName());
        assertEquals(TEACHER_ID, course.getTeacher().getId());
    }
}
