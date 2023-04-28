package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

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
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.modelmother.CourseModelMother;
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
    private CourseModel courseModel;
    
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
        
        courseModel = CourseModelMother.complete().build();
    }
    
    @Test
    void deassignTeacherToCourse_ShouldDeleteDependencies() throws ServiceException {
        TeacherModel teacherModel = TeacherModel.builder()
                .id(teacherEntity_2.getId()).build();
        CourseModel courseModel = CourseModelMother.complete()
                .id(courseEntity.getId().intValue())
                .teachers(new HashSet<>()).build();
        courseModel.getTeachers().add(teacherModel);
        
        courseService.deassignTeacherToCourse(courseModel);

        CourseEntity persistedCourse = courseRepository.findById(
                courseModel.getId().intValue());
        assertEquals(ZERO_SET_SIZE, persistedCourse.getTeachers().size());
    }
    
    @Test
    void assignTeacherToCourse_ShouldCreateDependencies() throws ServiceException {
        TeacherModel teacherModel = TeacherModel.builder()
                                                .id(teacherEntity_1.getId()).build();
        
        Set<TeacherModel> teachers = new HashSet<>();
        teachers.add(teacherModel);
        courseModel.setTeachers(teachers);
        courseModel.setId(courseEntity.getId());
        
        courseService.assignTeacherToCourse(courseModel);
        
        CourseEntity persistedCourse = courseRepository.findById(
                courseModel.getId().intValue());
        
        assertEquals(COURSE_QUANTITY, persistedCourse.getTeachers().size());
    }
}
