package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.RollbackException;
import jakarta.persistence.TransactionRequiredException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.TeacherEntity;

@Slf4j
@Repository
public class TeacherJdbcDao implements TeacherDao {
    
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public TeacherJdbcDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public TeacherEntity getCourseListByTeacherId(int id) throws DaoException {
        log.debug("Get courses list by teacher id={}.", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            TeacherEntity teacher = entityManager.find(TeacherEntity.class, id);
            teacher.getCourseList().iterator();
            log.trace("Courses list of teacher id={} was received");
            return teacher;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the course list data by the teacher id from the database failed.",
                                   e);
        }
    }
    
    @Override
    public void deleteById(int id) throws DaoException {
        log.debug("Delete teacher with id={}.", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            TeacherEntity teacher = new TeacherEntity();
            teacher.setId(id);
            entityManager.remove(teacher);
            log.trace("Teacher with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException e) {
            throw new DaoException("Deleting the database teacher data failed.", e);
        }
    }
    
    @Transactional
    @Override
    public void update(TeacherEntity entity) throws DaoException {
        log.debug("Udate teacher with id={}.", entity.getId());
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            log.trace("Teacher with id={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e){
            throw new DaoException("Updating the database teacher data failed.", e);
        }
    }
    
    @Override
    public TeacherEntity getById(int id) throws DaoException {
        log.debug("Get teacher with id={}.", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            TeacherEntity teacher = entityManager.find(TeacherEntity.class, id);
            log.trace("Teacher with id={} was received.", teacher.getId());
            return teacher;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the database teacher data failed.", e);
        }
    }
    
    @Transactional
    @Override
    public void insert(TeacherEntity entity) throws DaoException {
        log.debug("Insert teacher with id={} to the database.", entity.getId());
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            log.trace("Teacher with id={} was added to the database.", entity.getId());
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                 TransactionRequiredException | RollbackException e) {
            throw new DaoException("Inserting the database teacher data failed.", e);
        }
    }
}
