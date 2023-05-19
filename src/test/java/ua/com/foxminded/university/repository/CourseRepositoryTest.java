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
import ua.com.foxminded.university.entity.LessonEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.TeacherEntityMother;
import ua.com.foxminded.university.entitymother.LessonEntityMother;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class CourseRepositoryTest {
    
    private static final int TEACHERS_QUANTITY = 1;
    private static final int LESSONS_QUANTITY = 1;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private CourseRepository courseRepository;
    
    private CourseEntity course;
    private LessonEntity lesson;
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
        lesson = LessonEntityMother.complete()
                                         .course(course).build();
        entityManager.persist(lesson);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void getCourseRelationsById_ShouldContainAllDependencies() {
        CourseEntity persistedCourse = courseRepository.getCourseRelationsById(course.getId());
        LocalDate persistedDate = persistedCourse.getLessons()
                                                 .iterator().next().getDatestamp();
        
        assertEquals(lesson.getDatestamp(), persistedDate);
        assertEquals(LESSONS_QUANTITY, persistedCourse.getLessons().size());
        assertEquals(TEACHERS_QUANTITY, persistedCourse.getTeachers().size());
    }

    @Test
    void findTimetablesById_ShouldReturnCourseWithTimetableList_WhenEnterCourseId() {
        CourseEntity receivedCourse = courseRepository
                .findTimetablesById(course.getId());
        Set<LessonEntity> timetables = receivedCourse.getLessons();
        assertEquals(LESSONS_QUANTITY, timetables.size());
    }
    
    @Test
    void findById_ShouldReturnCourseWithId() {
        CourseEntity receivedCourse = courseRepository.findById(course.getId().intValue());
        assertEquals(course.getId(), receivedCourse.getId());
    }
}
