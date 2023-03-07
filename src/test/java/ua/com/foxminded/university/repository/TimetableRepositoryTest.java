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
    void findCourseById_ReceivingTimetableDatabaseData_CorrectReceivedData() 
            throws RepositoryException {
        TimetableEntity receivedTimetable = timetableRepository.findCourseById(
                timetable.getId());
        
        assertEquals(course.getDescription(), 
                     receivedTimetable.getCourse().getDescription());
        assertEquals(course.getId(), 
                     receivedTimetable.getCourse().getId());
        assertEquals(course.getName(), 
                     receivedTimetable.getCourse().getName());
        assertEquals(timetable.getDescription(), receivedTimetable.getDescription());
        assertEquals(timetable.getEndTime(), receivedTimetable.getEndTime());
        assertEquals(group.getId(), receivedTimetable.getGroup().getId());
        assertEquals(timetable.getStartTime(), receivedTimetable.getStartTime());
        assertEquals(timetable.getId(), receivedTimetable.getId());
        assertEquals(timetable.getDayOfWeek(), receivedTimetable.getDayOfWeek());
    }
        
    @Test
    void findGroupById_ReceivingTimetableDatabaseData_CorrectReceivedData() 
            throws RepositoryException {
        TimetableEntity receivedTimetable = timetableRepository.findGroupById(
                timetable.getId());
        
        assertEquals(group.getName(), receivedTimetable.getGroup().getName());
        assertEquals(course.getId(), receivedTimetable.getCourse().getId());
        assertEquals(timetable.getDescription(), receivedTimetable.getDescription());
        assertEquals(timetable.getEndTime(), receivedTimetable.getEndTime());
        assertEquals(group.getId(), receivedTimetable.getGroup().getId());
        assertEquals(timetable.getId(), receivedTimetable.getId());
        assertEquals(timetable.getStartTime(), receivedTimetable.getStartTime());
        assertEquals(timetable.getDayOfWeek(), receivedTimetable.getDayOfWeek());
    }
    
    @Test
    void findById_GettingTimetableById_CorrectRetrievedData() throws RepositoryException {
        TimetableEntity receivedTimetable = timetableRepository.findById(
                timetable.getId().intValue());
        
        assertEquals(timetable.getId(), receivedTimetable.getId());
        assertEquals(timetable.getDescription(), receivedTimetable.getDescription());
        assertEquals(timetable.getStartTime(), receivedTimetable.getStartTime());
        assertEquals(timetable.getEndTime(), receivedTimetable.getEndTime());
        assertEquals(course.getId(), receivedTimetable.getCourse().getId());
        assertEquals(group.getId(), receivedTimetable.getGroup().getId());
        assertEquals(timetable.getDayOfWeek(), receivedTimetable.getDayOfWeek());
    }
}
