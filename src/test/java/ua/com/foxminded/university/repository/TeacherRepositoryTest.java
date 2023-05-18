package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.TeacherEntityMother;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TeacherRepositoryTest {
    
    private static final int COURSES_QUANTITY = 1;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    private TeacherEntity teacher;
    private CourseEntity course;
    
    @BeforeEach
    void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        teacher = TeacherEntityMother.complete().build();
        entityManager.persist(teacher);
        course = CourseEntityMother.complete().build();
        course.setTeachers(new HashSet<>(Arrays.asList(teacher)));
        entityManager.persist(course);
        teacher.setCourses(new HashSet<>(Arrays.asList(course)));
        entityManager.persist(teacher);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void findCoursesById_ShouldReturnCoursesOwnedByTeacherWithId() {
        TeacherEntity receivedTeacher = teacherRepository.findById(teacher.getId().intValue());
        assertEquals(COURSES_QUANTITY, receivedTeacher.getCourses().size());
    }
    
    @Test
    void findById_ShouldReturnTeacherEntityWithId() {
        TeacherEntity receivedTeacher = teacherRepository.findById(teacher.getId().intValue());
        assertEquals(teacher.getId(), receivedTeacher.getId());
    }
}