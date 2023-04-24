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
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.TeacherEntityMother;
import ua.com.foxminded.university.entitymother.TimetableEntityMother;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class CourseRepositoryTest {
    
    private static final int TEACHERS_QUANTITY = 1;
    private static final int TIMETABLES_QUANTITY = 1;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private CourseRepository courseRepository;
    
    private CourseEntity course;
    private TimetableEntity timetable;
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
        timetable = TimetableEntityMother.complete()
                                         .course(course).build();
        entityManager.persist(timetable);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void getEagerlyById_ShouldContainAllDependencies() {
        CourseEntity persistedCourse = courseRepository.getCourseWithDependencies(course.getId());
        LocalDate persistedDate = persistedCourse.getTimetables()
                                                 .iterator().next().getDatestamp();
        
        assertEquals(timetable.getDatestamp(), persistedDate);
        assertEquals(TIMETABLES_QUANTITY, persistedCourse.getTimetables().size());
        assertEquals(TEACHERS_QUANTITY, persistedCourse.getTeachers().size());
    }

    @Test
    void findTimetablesById_ShouldReturnCourseWithTimetableList_WhenEnterCourseId() {
        CourseEntity receivedCourse = courseRepository
                .findTimetablesById(course.getId());
        Set<TimetableEntity> timetables = receivedCourse.getTimetables();
        assertEquals(TIMETABLES_QUANTITY, timetables.size());
    }
    
    @Test
    void findById_ShouldReturnCourseWithId() {
        CourseEntity receivedCourse = courseRepository.findById(course.getId().intValue());
        assertEquals(course.getId(), receivedCourse.getId());
    }
}
