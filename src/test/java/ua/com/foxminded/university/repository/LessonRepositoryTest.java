package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Teacher;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entitymother.CourseMother;
import ua.com.foxminded.university.entitymother.GroupMother;
import ua.com.foxminded.university.entitymother.TimingMother;
import ua.com.foxminded.university.entitymother.LessonMother;
import ua.com.foxminded.university.entitymother.TeacherMother;
import ua.com.foxminded.university.entitymother.TimetableMother;

@DataJpaTest
@ActiveProfiles("test")
class LessonRepositoryTest {
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private LessonRepository lessonRepository;
    
    private Course course;
    private Group group;
    private Lesson lesson;
    private Timetable timetable;
    private Timing timing;
    private Teacher teacher;
    
    @BeforeEach
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        timetable = TimetableMother.complete().build();
        entityManager.persist(timetable);
        
        timing = TimingMother.complete().timetable(timetable).build();
        entityManager.persist(timing);
        
        course = CourseMother.complete().build();
        entityManager.persist(course);
        
        group = GroupMother.complete().lessons(new HashSet<>()) .build();
        entityManager.persist(group);
        
        teacher = TeacherMother.complete().build();
        entityManager.persist(teacher);
        
        lesson = LessonMother.complete().course(course)
                                        .teacher(teacher)
                                        .groups(new HashSet<>()).build();
        lesson.addGroup(group);
       
        entityManager.persist(lesson);
        
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    void findByTeacherIdAndLessonOrderAndCourseId_ShouldReturnLesson() {
        Lesson persistedLesson = lessonRepository
                .findByDatestampAndTeacherIdAndLessonOrderAndCourseId(lesson.getDatestamp(),
                                                                      teacher.getId(), 
                                                                      lesson.getLessonOrder(), 
                                                                      course.getId());
        assertEquals(lesson, persistedLesson);
    }
    
    void findByDatestampAndGroupIdAndLessonTimingId_ShouldReturnLesson() {
        Lesson persistedLesson = lessonRepository.findByDatestampAndLessonOrderAndGroupsId(
                lesson.getDatestamp(), group.getId(), timing.getId());
        
        assertEquals(lesson.getGroups().iterator().next().getId(), 
                     persistedLesson.getGroups().iterator().next().getId());
    }
    
    @Test
    void findByDatestamp_ShouldReturnDayLessonsWithTimetableRelationship() {
        List<Lesson> lessons = lessonRepository.findByDatestamp(
                lesson.getDatestamp());
        assertEquals(lesson.getDatestamp(), lessons.iterator().next().getDatestamp());
    }
    
    @Test
    void findCourseById_ShouldReturnCourseOwnedByTimetableWithId() {
        Lesson receivedSchedule = lessonRepository.findCourseById(lesson.getId());
        assertEquals(course.getId(), receivedSchedule.getCourse().getId());
    }
        
    @Test
    void findGroupById_ShouldReturnGroupOwnedByTimetableWithId() {
        Lesson receivedSchedule = lessonRepository.findGroupById(
                lesson.getId());
        assertEquals(group.getId(), receivedSchedule.getGroups().iterator().next().getId());
    }
    
    @Test
    void findById_ShouldReturnTimetableEntityWithId() {
        Lesson receivedSchedule = lessonRepository.findById(
                lesson.getId().intValue());
        assertEquals(lesson.getId(), receivedSchedule.getId());
    }
}
