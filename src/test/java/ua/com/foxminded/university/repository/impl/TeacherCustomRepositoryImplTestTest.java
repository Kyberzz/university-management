package ua.com.foxminded.university.repository.impl;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.config.RepositoryConfigTest;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TeacherRepository;

@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = RepositoryConfigTest.class)
@ExtendWith(SpringExtension.class)
class TeacherCustomRepositoryImplTestTest {
    
    private static final String COURSE_DESCRIPTION = "some description";
    private static final String COURSE_NAME = "Programming";
    private static final String TEACHER_LAST_NAME = "Ritchie";
    private static final String TEACHER_FIRST_NAME = "Dennis";
    private static final int TEACHER_ID = 1;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @BeforeEach
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        TeacherEntity teacher = new TeacherEntity();
        teacher.setFirstName(TEACHER_FIRST_NAME);
        teacher.setLastName(TEACHER_LAST_NAME);
        entityManager.persist(teacher);
        
        CourseEntity course = new CourseEntity();
        course.setDescription(COURSE_DESCRIPTION);
        course.setName(COURSE_NAME);
        course.setTeacher(teacher);
        entityManager.persist(course);
        entityManager.getTransaction().commit();
    }

    @Test
    void findById_GettingTeacherById_CorrectRetrievedData() throws RepositoryException {
        TeacherEntity teacher = teacherRepository.findById(TEACHER_ID);
        
        assertEquals(TEACHER_ID, teacher.getId());
        assertEquals(TEACHER_FIRST_NAME, teacher.getFirstName());
        assertEquals(TEACHER_LAST_NAME, teacher.getLastName());
    }
}
