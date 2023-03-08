package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.objectmother.CourseEntityMother;
import ua.com.foxminded.university.objectmother.GroupEntityMother;
import ua.com.foxminded.university.objectmother.TimetableEntityMother;

@DataJpaTest
@ActiveProfiles("test")
class TimetableRepositoryTest {
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    private CourseEntity course;
    private GroupEntity group;
    private TimetableEntity timetable;
    
    @BeforeEach
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        course = CourseEntityMother.complete().build();
        entityManager.persist(course);
        
        group = GroupEntityMother.complete().build();
        entityManager.persist(group);
        
        timetable = TimetableEntityMother.complete().build();
        timetable.setCourse(course);
        timetable.setGroup(group);
        entityManager.persist(timetable);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void findCourseById_ShouldReturnCourseOwnedByTimetableWithId() 
            throws RepositoryException {
        TimetableEntity receivedTimetable = timetableRepository.findCourseById(
                timetable.getId());
        assertEquals(course.getId(), receivedTimetable.getCourse().getId());
    }
        
    @Test
    void findGroupById_ShouldReturnGroupOwnedByTimetableWithId() 
            throws RepositoryException {
        TimetableEntity receivedTimetable = timetableRepository.findGroupById(
                timetable.getId());
        assertEquals(group.getId(), receivedTimetable.getGroup().getId());
    }
    
    @Test
    void findById_ShouldReturnTimetableEntityWithId() throws RepositoryException {
        TimetableEntity receivedTimetable = timetableRepository.findById(
                timetable.getId().intValue());
        assertEquals(timetable.getId(), receivedTimetable.getId());
    }
}
