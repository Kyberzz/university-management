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
import ua.com.foxminded.university.entity.PersonEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.objectmother.CourseEntityMother;
import ua.com.foxminded.university.objectmother.TeacherEntityMother;

@DataJpaTest
@ActiveProfiles("test")
class TeacherRepositoryTest {
    
    private static final int FIST_ELEMENT = 0;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    private TeacherEntity teacher;
    private CourseEntity course;
    private PersonEntity person;
    
    @BeforeEach
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        teacher = TeacherEntityMother.complete().build();
        person = teacher.getUser().getPerson();
        entityManager.persist(teacher);
        
        course = CourseEntityMother.complete().build();
        course.setTeacher(teacher);
        entityManager.persist(course);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void findCourseListById_ReceivingTeacherDatabaseData_CorrectReceivedData() 
            throws RepositoryException {
        TeacherEntity receivedTeacher = teacherRepository.findCourseListById(teacher.getId());
        
        assertEquals(teacher.getId(), receivedTeacher.getId());
        assertEquals(person.getFirstName(), 
                     receivedTeacher.getUser().getPerson().getFirstName());
        assertEquals(person.getLastName(), 
                     receivedTeacher.getUser().getPerson().getLastName());
        assertEquals(teacher.getId(), 
                     receivedTeacher.getCourses().get(FIST_ELEMENT).getTeacher().getId());
        assertEquals(course.getId(), 
                     receivedTeacher.getCourses().get(FIST_ELEMENT).getId());
        assertEquals(course.getName(), 
                     receivedTeacher.getCourses().get(FIST_ELEMENT).getName());
        assertEquals(course.getDescription(), 
                     receivedTeacher.getCourses().get(FIST_ELEMENT).getDescription());
    }
    
    @Test
    void findById_GettingTeacherById_CorrectRetrievedData() throws RepositoryException {
        TeacherEntity receivedTeacher = teacherRepository.findById(teacher.getId().intValue());
        
        assertEquals(teacher.getId(), receivedTeacher.getId());
        assertEquals(person.getFirstName(), 
                     receivedTeacher.getUser().getPerson().getFirstName());
        assertEquals(person.getLastName(), 
                     receivedTeacher.getUser().getPerson().getLastName());
    }
}
