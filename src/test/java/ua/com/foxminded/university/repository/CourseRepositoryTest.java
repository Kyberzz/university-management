package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.TimetableEntityMother;
import ua.com.foxminded.university.exception.RepositoryException;

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryTest {
    
    private static final int TIMETABLES_QUANTITY = 1;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private CourseRepository courseRepository;
    
    private CourseEntity course;
    private TimetableEntity timetable;
    
    @BeforeEach
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        course = CourseEntityMother.complete().build();
        entityManager.persist(course);
        
        timetable = TimetableEntityMother.complete()
                                         .course(course)
                                         .build();
        entityManager.persist(timetable);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    void findTimetableListById_ShouldReturnCourseWithTimetableList_WhenEnterCourseId() 
            throws RepositoryException {
        CourseEntity receivedCourse = courseRepository.findTimetableListById(course.getId());
        assertEquals(TIMETABLES_QUANTITY, receivedCourse.getTimetables().size());
    }
    
    @Test
    void findById_ShouldReturnCourseWithId() throws RepositoryException {
        CourseEntity receivedCourse = courseRepository.findById(course.getId().intValue());
        assertEquals(course.getId(), receivedCourse.getId());
    }
}
