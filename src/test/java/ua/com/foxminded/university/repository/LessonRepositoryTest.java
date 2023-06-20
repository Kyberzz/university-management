package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;

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

import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Teacher;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.CourseMother;
import ua.com.foxminded.university.entitymother.GroupMother;
import ua.com.foxminded.university.entitymother.LessonMother;
import ua.com.foxminded.university.entitymother.TimetableMother;
import ua.com.foxminded.university.entitymother.TimingMother;
import ua.com.foxminded.university.entitymother.UserMother;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
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
    private User user;
    
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
        
        group = GroupMother.complete().build();
        entityManager.persist(group);
        
        user = UserMother.complete().build();
        entityManager.persist(user);
        
        teacher = Teacher.builder().user(user).build();
        entityManager.persist(teacher);
        
        lesson = LessonMother.complete().course(course)
                                        .teacher(teacher)
                                        .groups(new HashSet<>()).build();
        group.setLessons(new HashSet<>());
        lesson.addGroup(group);
       
        entityManager.persist(lesson);
        
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void findByGroupsId_ShouldReturnLessonsOfGroup() {
        List<Lesson> lessons = lessonRepository.findByGroupsId(group.getId());
        assertEquals(lesson.getId(), lessons.iterator().next().getId());
    }
    
    @Test
    void findByDatestampAndGroupsIdAndLessonOrder_ShouldReturnLessons() {
        Lesson persistedLesson = lessonRepository.findByDatestampAndGroupsIdAndLessonOrder(
                lesson.getDatestamp(), group.getId(), lesson.getLessonOrder());
        assertEquals(lesson.getId(), persistedLesson.getId());
    }
    
    @Test
    void  findByGroupId_ShouldReturenLessonsOwnedByGroup() {
        List<Lesson> lessons = lessonRepository.findByDatestampAndGroupsId(
                lesson.getDatestamp(), group.getId());
        assertEquals(group.getId(), lessons.iterator().next().getId());
    }
    
    @Test
    void findByTeacherId_ShouldReturnLessonsOwnedByTeacher() {
        List<Lesson> lessons = lessonRepository.findByTeacherId(teacher.getId());
        assertEquals(lesson, lessons.iterator().next());
    }
    
    @Test
    void findByDatestampAndTeacherId_ShouldReturnLessons() {
        List<Lesson> lessons = lessonRepository.findByDatestampAndTeacherId(
                lesson.getDatestamp(), teacher.getId());
        
        assertEquals(lesson, lessons.iterator().next());
    }
    
    @Test
    void findByDatestampAndTeacherIdAndLessonOrder_ShouldReturnLessons() {
        Lesson lessons = lessonRepository.findByDatestampAndTeacherIdAndLessonOrder(
                lesson.getDatestamp(), lesson.getTeacher().getId(), lesson.getLessonOrder());
        
        assertEquals(lesson, lessons);
    }
    
    
    @Test
    void findByTeacherIdAndLessonOrderAndCourseId_ShouldReturnLesson() {
        Lesson persistedLesson = lessonRepository
                .findByDatestampAndTeacherIdAndLessonOrderAndCourseId(lesson.getDatestamp(),
                                                                      teacher.getId(), 
                                                                      lesson.getLessonOrder(), 
                                                                      course.getId());
        assertEquals(lesson, persistedLesson);
    }
    
    @Test
    void findByDatestampAndGroupIdAndLessonTimingId_ShouldReturnLesson() {
        Lesson persistedLesson = lessonRepository.findByDatestampAndLessonOrderAndGroupsId(
                lesson.getDatestamp(),
                lesson.getLessonOrder(),
                group.getId());
        
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
