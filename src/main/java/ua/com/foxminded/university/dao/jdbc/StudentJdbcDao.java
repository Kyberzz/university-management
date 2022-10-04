package ua.com.foxminded.university.dao.jdbc;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.RollbackException;
import jakarta.persistence.TransactionRequiredException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.entity.StudentEntity;

@Slf4j
@Repository
public class StudentJdbcDao implements StudentDao {
    
    private EntityManagerFactory entityManagerFactory;
    
    public StudentJdbcDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public StudentEntity getGroupByStudentId(int id) throws DaoException {
        log.debug("Get group by student id={}.", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            StudentEntity student = entityManager.find(StudentEntity.class, id);
            student.getGroup().getId();
            log.trace("Group having student id={} was received.", student.getId());
            return student;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting group by the student id failed.", e);
        }
    }
    
    @Transactional
    @Override
    public void insert(StudentEntity entity) throws DaoException {
        log.debug("Insert student with id={}.", entity.getId());
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            log.trace("Student with id={} was inserted.", entity.getId());
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                 TransactionRequiredException | RollbackException e) {
            throw new DaoException("Inserting the student to the database failed.", e);
        }
    }
    
    @Override
    public StudentEntity getById(int id) throws DaoException {
        log.debug("Get student by id={}.", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            StudentEntity student = entityManager.find(StudentEntity.class, id);
            log.trace("Student with id={} was received.", student.getId());
            return student;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the student by its id failed.", e);
        }
    }
    
    @Transactional
    @Override
    public void update(StudentEntity entity) throws DaoException {
        log.debug("Update student with id={}.", entity.getId());
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            log.debug("Student with id={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e) {
            throw new DaoException("Updating the student data failed.", e);
        }
    }

    @Override
    public void deleteById(int id) throws DaoException {
        log.debug("Delete student by id={}.", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();){
            StudentEntity student = new StudentEntity();
            student.setId(id);
            entityManager.remove(student);
            log.trace("Student with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException e) {
            throw new DaoException("Deleting the student data failed.", e);
        }
    }
}
