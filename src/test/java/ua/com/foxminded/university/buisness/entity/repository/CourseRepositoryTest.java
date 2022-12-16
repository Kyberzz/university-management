package ua.com.foxminded.university.buisness.entity.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import ua.com.foxminded.university.buisness.BuisnessLayerTestSpringConfig;
import ua.com.foxminded.university.buisness.entity.CourseEntity;
import ua.com.foxminded.university.buisness.entity.DayOfWeek;
import ua.com.foxminded.university.buisness.entity.GroupEntity;
import ua.com.foxminded.university.buisness.entity.TeacherEntity;
import ua.com.foxminded.university.buisness.entity.TimetableEntity;

@Transactional
@ContextConfiguration(classes = BuisnessLayerTestSpringConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class CourseRepositoryTest {
    
    private static final String GROUP_NAME = "lk-89";
    private static final String WEEK_DAY = "THURSDAY";
    private static final String TEACHER_LAST_NAME = "Fock";
    private static final String TEACHER_FIRST_NAME = "Stiven";
    private static final String COURSE_NAME = "Programming";
    private static final String TIMETABLE_DESCRIPTION = "some description";
    private static final String COURSE_DESCRIPTION = "some description";
    private static final long END_TIME = 39360000;
    private static final long START_TIME = 36360000;
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
        teacher.setFirstName(TEACHER_FIRST_NAME);
        teacher.setLastName(TEACHER_LAST_NAME);
        entityManager.persist(teacher);
        
        CourseEntity course = new CourseEntity();
        course.setName(COURSE_NAME);
        course.setDescription(COURSE_DESCRIPTION);
        course.setTeacher(teacher);
        entityManager.persist(course);
        
        TimetableEntity timetable = new TimetableEntity();
        timetable.setGroup(group);
        timetable.setCourse(course);
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setEndTime(END_TIME);
        timetable.setStartTime(START_TIME);
        timetable.setWeekDay(DayOfWeek.valueOf(WEEK_DAY));
        entityManager.persist(timetable);
        entityManager.getTransaction().commit();
    }

    @Test
    void findTimetableListById_GettingDatabaseTimetableData_CorrectData() throws RepositoryException {
        CourseEntity receivedCourse = courseRepository.findTimetableListById(COURSE_ID);
        
        assertEquals(COURSE_ID, receivedCourse.getId());
        assertEquals(COURSE_NAME, receivedCourse.getName());
        assertEquals(TEACHER_ID, receivedCourse.getTeacher().getId());
        assertEquals(COURSE_ID, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getCourse().getId());
        assertEquals(START_TIME, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getStartTime());
        assertEquals(END_TIME, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getEndTime());
        assertEquals(GROUP_ID, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getGroup().getId());
        assertEquals(TIMETABLE_ID, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getId());
        assertEquals(WEEK_DAY, receivedCourse.getTimetableList().get(FIRST_ELEMENT).getWeekDay()
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
