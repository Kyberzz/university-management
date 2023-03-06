package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.config.RepositoryTestConfig;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.objectmother.CourseEntityMother;
import ua.com.foxminded.university.objectmother.GroupEntityMother;
import ua.com.foxminded.university.objectmother.TeacherEntityMother;
import ua.com.foxminded.university.objectmother.TimetableEntityMother;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryTestConfig.class)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CourseRepositoryTest {
    
    private static final int FIRST_ELEMENT = 0;
    
    @PersistenceContext
    private EntityManager entityManager;
    
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
        GroupEntity group = GroupEntityMother.complete().build();
        entityManager.persist(group);
        
        TeacherEntity teacher = TeacherEntityMother.complete().build();
        entityManager.persist(teacher);
        
        course = CourseEntityMother.complete()
                                   .teacher(teacher)
                                   .build();
        entityManager.persist(course);

        TimetableEntity timetable = TimetableEntityMother.complete()
                                                         .group(group)
                                                         .course(course)
                                                         .build();
        entityManager.persist(timetable);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    void findTimetableListById_GettingDatabaseTimetableData_CorrectData() 
            throws RepositoryException {
        CourseEntity receivedCourse = courseRepository.findTimetableListById(course.getId());
        
        assertEquals(course.getId(), receivedCourse.getId());
        assertEquals(course.getName(), receivedCourse.getName());
        assertEquals(course.getTeacher().getId(), receivedCourse.getTeacher().getId());
        assertEquals(timetable.getId(), 
                     receivedCourse.getTimetableList().get(FIRST_ELEMENT).getCourse().getId());
        assertEquals(timetable.getStartTime(), 
                     receivedCourse.getTimetableList().get(FIRST_ELEMENT).getStartTime());
        assertEquals(timetable.getEndTime(), 
                     receivedCourse.getTimetableList().get(FIRST_ELEMENT).getEndTime());
        assertEquals(timetable.getGroup().getId(), 
                     receivedCourse.getTimetableList().get(FIRST_ELEMENT).getGroup().getId());
        assertEquals(timetable.getId(), 
                     receivedCourse.getTimetableList().get(FIRST_ELEMENT).getId());
        assertEquals(timetable.getDayOfWeek(), 
                     receivedCourse.getTimetableList().get(FIRST_ELEMENT).getDayOfWeek());
    }
    
    @Test
    void findById_ShouldReturnUserEntity() throws RepositoryException {
        CourseEntity receivedCourse = courseRepository.findById(course.getId().intValue());
        assertEquals(course.getId(), receivedCourse.getId());
        assertEquals(course.getDescription(), receivedCourse.getDescription());
        assertEquals(course.getName(), receivedCourse.getName());
        assertEquals(course.getTeacher().getId(), receivedCourse.getTeacher().getId());
    }
}
