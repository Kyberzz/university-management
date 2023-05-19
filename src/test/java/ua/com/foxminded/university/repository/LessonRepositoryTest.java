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
import ua.com.foxminded.university.entity.LessonTimingEntity;
import ua.com.foxminded.university.entity.LessonEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.GroupEntityMother;
import ua.com.foxminded.university.entitymother.LessonTimingEntityMother;
import ua.com.foxminded.university.entitymother.LessonEntityMother;
import ua.com.foxminded.university.entitymother.TimetableEntityMother;

@DataJpaTest
@ActiveProfiles("test")
class LessonRepositoryTest {
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private LessonRepository scheduleRepository;
    
    private CourseEntity course;
    private GroupEntity group;
    private LessonEntity lesson;
    private TimetableEntity timetable;
    private LessonTimingEntity lessonTiming;
    
    @BeforeEach
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        timetable = TimetableEntityMother.complete().build();
        entityManager.persist(timetable);
        
        lessonTiming = LessonTimingEntityMother.complete().timetable(timetable).build();
        entityManager.persist(lessonTiming);
        
        course = CourseEntityMother.complete().build();
        entityManager.persist(course);
        
        group = GroupEntityMother.complete().build();
        entityManager.persist(group);
        
        lesson = LessonEntityMother.complete().course(course)
                                              .group(group).build();
        entityManager.persist(lesson);
        
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    void findByDatestampAndGroupIdAndLessonTimingId_ShouldReturnScheduleEntity() {
        LessonEntity entity = scheduleRepository.findByDatestampAndGroupIdAndTimingId(
                lesson.getDatestamp(), group.getId(), lessonTiming.getId());
        assertEquals(lesson.getGroup().getId(), entity.getGroup().getId());
    }
    
    @Test
    void findByDatestamp_ShouldReturnDayLessonsWithTimetableRelationship() {
        List<LessonEntity> lessons = scheduleRepository.findByDatestamp(
                lesson.getDatestamp());
        assertEquals(lesson.getDatestamp(), lessons.iterator().next().getDatestamp());
    }
    
    @Test
    void findCourseById_ShouldReturnCourseOwnedByTimetableWithId() {
        LessonEntity receivedSchedule = scheduleRepository.findCourseById(lesson.getId());
        assertEquals(course.getId(), receivedSchedule.getCourse().getId());
    }
        
    @Test
    void findGroupById_ShouldReturnGroupOwnedByTimetableWithId() {
        LessonEntity receivedSchedule = scheduleRepository.findGroupById(
                lesson.getId());
        assertEquals(group.getId(), receivedSchedule.getGroup().getId());
    }
    
    @Test
    void findById_ShouldReturnTimetableEntityWithId() {
        LessonEntity receivedSchedule = scheduleRepository.findById(
                lesson.getId().intValue());
        assertEquals(lesson.getId(), receivedSchedule.getId());
    }
}
