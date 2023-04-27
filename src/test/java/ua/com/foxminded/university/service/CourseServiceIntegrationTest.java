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
class CourseServiceIntegrationTest {
    
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
        teacherEntity_1 = TeacherEntityMother.complete().build();
        teacherEntity_2 = TeacherEntityMother.complete().build();
        entityManager.persist(teacherEntity_1);
        entityManager.persist(teacherEntity_2);
        
        courseEntity = CourseEntityMother.complete().build();
        entityManager.persist(courseEntity);
        entityManager.getTransaction().commit();
        entityManager.close();
        
        courseModel = CourseModelMother.complete().build();
    }
    
    @Test
    void addTeacherToCourse() throws ServiceException {
        TeacherModel teacherModel_1 = TeacherModel.builder()
                                                  .id(teacherEntity_1.getId()).build();
        TeacherModel teacherModel_2 = TeacherModel.builder()
                                                  .id(teacherEntity_2.getId()).build();
        
        Set<TeacherModel> teachers = new HashSet<>();
        teachers.add(teacherModel_1);
        teachers.add(teacherModel_2);
        courseModel.setTeachers(teachers);
        courseModel.setId(courseEntity.getId());
        
        courseService.addTeacherToCourse(courseModel);
        
        CourseEntity persistedCourse = courseRepository.findById(
                courseModel.getId().intValue());
        
        assertEquals(COURSE_QUANTITY, persistedCourse.getTeachers().size());
    }
}
