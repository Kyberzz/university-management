package ua.com.foxminded.university.dao.jdbc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.entity.CourseEntity;

@Slf4j
@Repository
public class CourseRepository implements CourseDao {
    
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public CourseRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public CourseEntity getTimetableListByCourseId(int id) throws DaoException {
        log.debug("Get timetable list by course id={}", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CourseEntity course = entityManager.find(CourseEntity.class, id);
            course.getTimetableList().iterator();
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Timetable list of course with id={} was received.", course.getId());
            return course;
        } catch (IllegalStateException| RollbackException e) {
            throw new DaoException("Getting the timetable list by course id failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws DaoException {
        log.debug("Delete course by id={}", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            CourseEntity course = entityManager.find(CourseEntity.class, id);
            entityManager.remove(course);
            entityManager.close();
            log.trace("Course with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Deleting the course by its id failed.", e);
        }
    }
    
    @Override
    public void update(CourseEntity entity) throws DaoException {
        log.debug("Update course with id={}", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Course with id ={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e) {
            throw new DaoException("Updating the course data failed.", e);
        }
    }
    
    @Override
    public CourseEntity getById(int id) throws DaoException {
        log.debug("Get course by id={}", id);
        
        EntityManager entityManager = null;
        
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CourseEntity courseEntity = entityManager.find(CourseEntity.class, id);
            entityManager.close();
            log.trace("Course with id={} was received.", courseEntity.getId());
            entityManager.close();
            return courseEntity;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the course by its id failed.", e);
        }
    }
    
    @Override
    public CourseEntity insert(CourseEntity entity) throws DaoException {
        log.debug("Insert course with id={}", entity.getId());
       
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.refresh(entity);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Course with id={} was inserted.", entity.getId());
            return entity;
        } catch (IllegalStateException | IllegalArgumentException | EntityExistsException | 
                 TransactionRequiredException | RollbackException  e) {
            throw new DaoException("Inserting the course to the database failed.", e);
        }
    }
}
