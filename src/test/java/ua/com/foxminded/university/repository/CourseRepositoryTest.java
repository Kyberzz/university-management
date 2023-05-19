package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.ScheduleEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.TeacherEntityMother;
import ua.com.foxminded.university.entitymother.ScheduleEntityMother;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class CourseRepositoryTest {
    
    private static final int TEACHERS_QUANTITY = 1;
    private static final int SCHEDULES_QUANTITY = 1;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private CourseRepository courseRepository;
    
    private CourseEntity course;
    private ScheduleEntity schedule;
    private TeacherEntity teacher;
    
    @BeforeEach
    void init() {
        course = CourseEntityMother.complete().build();
        teacher = TeacherEntityMother.complete().build();
        
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(course);
        teacher.setCourses(new HashSet<>());
        teacher.getCourses().add(course);
        
        entityManager.persist(teacher);
        schedule = ScheduleEntityMother.complete()
                                         .course(course).build();
        entityManager.persist(schedule);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void getCourseRelationsById_ShouldContainAllDependencies() {
        CourseEntity persistedCourse = courseRepository.getCourseRelationsById(course.getId());
        LocalDate persistedDate = persistedCourse.getSchedules()
                                                 .iterator().next().getDatestamp();
        
        assertEquals(schedule.getDatestamp(), persistedDate);
        assertEquals(SCHEDULES_QUANTITY, persistedCourse.getSchedules().size());
        assertEquals(TEACHERS_QUANTITY, persistedCourse.getTeachers().size());
    }

    @Test
    void findTimetablesById_ShouldReturnCourseWithTimetableList_WhenEnterCourseId() {
        CourseEntity receivedCourse = courseRepository
                .findTimetablesById(course.getId());
        Set<ScheduleEntity> timetables = receivedCourse.getSchedules();
        assertEquals(SCHEDULES_QUANTITY, timetables.size());
    }
    
    @Test
    void findById_ShouldReturnCourseWithId() {
        CourseEntity receivedCourse = courseRepository.findById(course.getId().intValue());
        assertEquals(course.getId(), receivedCourse.getId());
    }
}
