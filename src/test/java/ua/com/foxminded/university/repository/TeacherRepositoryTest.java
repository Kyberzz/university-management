package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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

import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Teacher;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.CourseMother;
import ua.com.foxminded.university.entitymother.TeacherMother;
import ua.com.foxminded.university.entitymother.UserMother;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TeacherRepositoryTest {
    
    private static final int TEACHERS_QUANTITY = 1;
    private static final int COURSES_QUANTITY = 1;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    private Teacher teacher;
    private Course course;
    private User user;
    
    @BeforeEach
    void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        user = UserMother.complete().build();
        entityManager.persist(user);
        teacher = TeacherMother.complete()
                               .user(user).build();
        entityManager.persist(teacher);
        course = CourseMother.complete().build();
        course.setTeachers(new HashSet<>(Arrays.asList(teacher)));
        entityManager.persist(course);
        teacher.setCourses(new HashSet<>(Arrays.asList(course)));
        entityManager.persist(teacher);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void findByCoursesId_ShouldReturnTeachersHavingCourseId() {
        List<Teacher> teachers = teacherRepository.findByCoursesId(course.getId());
        assertEquals(TEACHERS_QUANTITY, teachers.size());
    }
    
    @Test
    void findByUserId_ShouldFindByIdPropertyOfRelatedEntityUser() {
        Teacher persistedTeacher = teacherRepository.findByUserId(user.getId());
        assertEquals(user.getId(), persistedTeacher.getUser().getId());
    }
    
    @Test
    void findCoursesById_ShouldReturnCoursesOwnedByTeacherWithId() {
        Teacher receivedTeacher = teacherRepository.findById(teacher.getId().intValue());
        assertEquals(COURSES_QUANTITY, receivedTeacher.getCourses().size());
    }
    
    @Test
    void findById_ShouldReturnTeacherEntityWithId() {
        Teacher receivedTeacher = teacherRepository.findById(teacher.getId().intValue());
        assertEquals(teacher.getId(), receivedTeacher.getId());
    }
}
