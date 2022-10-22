package ua.com.foxminded.university.repository.jdbc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.StudentRepository;

@Slf4j
@Repository
public class StudentJdbcRepository implements StudentRepository {
    
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public StudentJdbcRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public StudentEntity getGroupByStudentId(int id) throws RepositoryException {
        log.debug("Get group by student id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            StudentEntity student = entityManager.find(StudentEntity.class, id);
            student.getGroup().getName();
            entityManager.close();
            log.trace("Group having student id={} was received.", student.getId());
            return student;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new RepositoryException("Getting group by the student id failed.", e);
        }
    }
    
    @Override
    public StudentEntity insert(StudentEntity entity) throws RepositoryException {
        log.debug("Insert student with id={}.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.persist(entity);
            entityManager.close();
            log.trace("Student with id={} was inserted.", entity.getId());
            return entity;
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                TransactionRequiredException | RollbackException e) {
            throw new RepositoryException("Inserting the student to the database failed.", e);
        }
    }
    
    @Override
    public StudentEntity getById(int id) throws RepositoryException {
        log.debug("Get student by id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            StudentEntity student = entityManager.find(StudentEntity.class, id);
            entityManager.close();
            log.trace("Student with id={} was received.", student.getId());
            return student;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new RepositoryException("Getting the student by its id failed.", e);
        }
    }
    
    @Override
    public void update(StudentEntity entity) throws RepositoryException {
        log.debug("Update student with id={}.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.merge(entity);
            entityManager.close();
            log.debug("Student with id={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e) {
            throw new RepositoryException("Updating the student data failed.", e);
        }
    }

    @Override
    public void deleteById(int id) throws RepositoryException {
        log.debug("Delete student by id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            StudentEntity student = entityManager.find(StudentEntity.class, id) ;
            entityManager.remove(student);
            entityManager.close();
            log.trace("Student with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Deleting the student data failed.", e);
        }
    }
}
