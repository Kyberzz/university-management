package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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
import ua.com.foxminded.university.entity.ScheduleEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.GroupEntityMother;
import ua.com.foxminded.university.entitymother.ScheduleEntityMother;

@DataJpaTest
@ActiveProfiles("test")
class ScheduleRepositoryTest {
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private ScheduleRepository timetableRepository;
    
    private CourseEntity course;
    private GroupEntity group;
    private ScheduleEntity schedule;
    
    @BeforeEach
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        course = CourseEntityMother.complete().build();
        entityManager.persist(course);
        
        group = GroupEntityMother.complete().build();
        entityManager.persist(group);
        
        schedule = ScheduleEntityMother.complete().build();
        schedule.setCourse(course);
        schedule.setGroup(group);
        entityManager.persist(schedule);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
//    void findByDatestampAndStartTimeAndGroupId_ShouldReturnTimetableEntity() {
//        ScheduleEntity entity = timetableRepository.findByDatestampAndLessonOrderAndGroupId(
//                schedule.getDatestamp(), schedule.getLessonOrder(), group.getId());
//        assertEquals(schedule.getGroup().getId(), entity.getGroup().getId());
//    }
    
    @Test
    void findByDatestamp_ShouldReturnTimetablesOfDay() {
        List<ScheduleEntity> timetables = timetableRepository.findByDatestamp(
                schedule.getDatestamp());
        assertEquals(schedule.getDatestamp(), timetables.iterator().next().getDatestamp());
    }
    
    @Test
    void findCourseById_ShouldReturnCourseOwnedByTimetableWithId() {
        ScheduleEntity receivedTimetable = timetableRepository.findCourseById(
                schedule.getId());
        assertEquals(course.getId(), receivedTimetable.getCourse().getId());
    }
        
    @Test
    void findGroupById_ShouldReturnGroupOwnedByTimetableWithId() {
        ScheduleEntity receivedTimetable = timetableRepository.findGroupById(
                schedule.getId());
        assertEquals(group.getId(), receivedTimetable.getGroup().getId());
    }
    
    @Test
    void findById_ShouldReturnTimetableEntityWithId() {
        ScheduleEntity receivedTimetable = timetableRepository.findById(
                schedule.getId().intValue());
        assertEquals(schedule.getId(), receivedTimetable.getId());
    }
}