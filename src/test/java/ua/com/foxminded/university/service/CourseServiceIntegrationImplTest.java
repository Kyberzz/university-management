package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.TeacherEntityMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.CourseRepository;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class CourseServiceIntegrationImplTest {
    public static final int ZERO_SET_SIZE = 0;
    public static final int COURSE_QUANTITY = 2;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CourseRepository courseRepository;
    
    private TeacherEntity teacherEntity_1;
    private TeacherEntity teacherEntity_2;
    private CourseEntity courseEntity;
    
    @BeforeEach
    void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        courseEntity = CourseEntityMother.complete().teachers(new HashSet<>()).build();
        teacherEntity_1 = TeacherEntityMother.complete().build();
        teacherEntity_2 = TeacherEntityMother.complete().courses(new HashSet<>()).build();
        
        entityManager.persist(teacherEntity_1);
        entityManager.persist(teacherEntity_2);

        teacherEntity_2.getCourses().add(courseEntity);
        courseEntity.getTeachers().add(teacherEntity_2);
        entityManager.persist(courseEntity);

        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void deassignTeacherToCourse_ShouldDeleteDependencies() throws ServiceException {
        courseService.deassignTeacherToCourse(teacherEntity_2.getId(), courseEntity.getId());

        CourseEntity persistedCourse = courseRepository.findById(
                courseEntity.getId().intValue());
        assertEquals(ZERO_SET_SIZE, persistedCourse.getTeachers().size());
    }
    
    @Test
    void assignTeacherToCourse_ShouldCreateDependencies() throws ServiceException {
        courseService.assignTeacherToCourse(teacherEntity_1.getId().intValue(), 
                                            courseEntity.getId().intValue());
        
        CourseEntity persistedCourse = courseRepository.findById(
                courseEntity.getId().intValue());
        
        assertEquals(COURSE_QUANTITY, persistedCourse.getTeachers().size());
    }
}
