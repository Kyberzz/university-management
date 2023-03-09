package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.TeacherEntityMother;
import ua.com.foxminded.university.exception.RepositoryException;

@DataJpaTest
@ActiveProfiles("test")
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
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        teacher = TeacherEntityMother.complete().build();
        entityManager.persist(teacher);
        
        course = CourseEntityMother.complete().teacher(teacher).build();
        entityManager.persist(course);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void findCoursesById_ShouldReturnCoursesOwnedByTeacherWithId() 
            throws RepositoryException {
        TeacherEntity receivedTeacher = teacherRepository.findCoursesById(teacher.getId());
        assertEquals(COURSES_QUANTITY, receivedTeacher.getCourses().size());
    }
    
    @Test
    void findById_ShouldReturnTeacherEntityWithId() throws RepositoryException {
        TeacherEntity receivedTeacher = teacherRepository.findById(teacher.getId().intValue());
        assertEquals(teacher.getId(), receivedTeacher.getId());
    }
}
