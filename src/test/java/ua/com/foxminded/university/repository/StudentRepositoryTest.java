package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.GroupEntityMother;
import ua.com.foxminded.university.entitymother.StudentEntityMother;
import ua.com.foxminded.university.entitymother.UserEntityMother;

@DataJpaTest
@ActiveProfiles("test")
class StudentRepositoryTest {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    private StudentEntity student;
    private GroupEntity group;
    private UserEntity user;
    
    @BeforeEach 
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        group = GroupEntityMother.complete().build();
        entityManager.persist(group);
        
        user = UserEntityMother.complete().build();
        entityManager.persist(user);
        
        student = StudentEntityMother.complete()
                                     .group(group)
                                     .user(user)
                                     .build();
        entityManager.persist(student);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    @Test
    void findGroupById_ShouldRetrunGroupByStudentId() {
        StudentEntity persistedStudent = studentRepository.findGroupById(student.getId());
        assertEquals(group.getId(), persistedStudent.getGroup().getId());
    }
    
    @Test
    void findById_ShouldReturnStudentObjectWithId() {
        StudentEntity persistedStudent = studentRepository.findById(student.getId());
        assertEquals(student.getId(), persistedStudent.getId());
    }
}
