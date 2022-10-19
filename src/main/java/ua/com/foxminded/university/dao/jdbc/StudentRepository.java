package ua.com.foxminded.university.dao.jdbc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.entity.StudentEntity;

@Slf4j
@Repository
public class StudentRepository implements StudentDao {
    
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public StudentRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public StudentEntity getGroupByStudentId(int id) throws DaoException {
        log.debug("Get group by student id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            StudentEntity student = entityManager.find(StudentEntity.class, id);
            student.getGroup().getId();
            entityManager.close();
            log.trace("Group having student id={} was received.", student.getId());
            return student;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting group by the student id failed.", e);
        }
    }
    
    @Override
    public StudentEntity insert(StudentEntity entity) throws DaoException {
        log.debug("Insert student with id={}.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.refresh(entity);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Student with id={} was inserted.", entity.getId());
            return entity;
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                TransactionRequiredException | RollbackException e) {
            throw new DaoException("Inserting the student to the database failed.", e);
        }
    }
    
    @Override
    public StudentEntity getById(int id) throws DaoException {
        log.debug("Get student by id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            StudentEntity student = entityManager.find(StudentEntity.class, id);
            entityManager.close();
            log.trace("Student with id={} was received.", student.getId());
            return student;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the student by its id failed.", e);
        }
    }
    
    @Override
    public void update(StudentEntity entity) throws DaoException {
        log.debug("Update student with id={}.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.debug("Student with id={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e) {
            throw new DaoException("Updating the student data failed.", e);
        }
    }

    @Override
    public void deleteById(int id) throws DaoException {
        log.debug("Delete student by id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            StudentEntity student = entityManager.find(StudentEntity.class, id) ;
            entityManager.remove(student);
            entityManager.close();
            log.trace("Student with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException e) {
            throw new DaoException("Deleting the student data failed.", e);
        }
    }
}
