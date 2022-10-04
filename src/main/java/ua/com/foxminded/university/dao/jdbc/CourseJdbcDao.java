package ua.com.foxminded.university.dao.jdbc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.RollbackException;
import jakarta.persistence.TransactionRequiredException;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.entity.CourseEntity;

@Slf4j
@Repository
public class CourseJdbcDao implements CourseDao {
    
   // @PersistenceContext
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public CourseJdbcDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public CourseEntity getTimetableListByCourseId(int id) throws DaoException {
        log.debug("Get timetable list by course id={}", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            entityManager.getTransaction().begin();
            CourseEntity course = entityManager.find(CourseEntity.class, id);
            course.getTimetableList().iterator();
            entityManager.getTransaction().commit();
            log.trace("Timetable list of course with id={} was received.", course.getId());
            return course;
        } catch (IllegalStateException | RollbackException e) {
            throw new DaoException("Getting the timetable list by course id failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws DaoException {
        log.debug("Delete course by id={}", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            CourseEntity course = new CourseEntity();
            course.setId(id);
            entityManager.remove(course);
            log.trace("Course with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Deleting the course by its id failed.", e);
        }
    }
    
    @Transactional
    @Override
    public void update(CourseEntity entity) throws DaoException {
        log.debug("Update course with id={}", entity.getId());
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            log.trace("Course with id ={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e) {
            throw new DaoException("Updating the course data failed.", e);
        }
    }
    
    @Override
    public CourseEntity getById(int id) throws DaoException {
        log.debug("Get course by id={}", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();){
            CourseEntity courseEntity = entityManager.find(CourseEntity.class, id);
            log.trace("Course with id={} was received.", courseEntity.getId());
            return courseEntity;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the course by its id failed.", e);
        }
    }
    
    @Transactional
    @Override
    public void insert(CourseEntity entity) throws DaoException {
        log.debug("Insert course with id={}", entity.getId());
       
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            log.trace("Course with id={} was inserted.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | EntityExistsException | 
                 TransactionRequiredException | RollbackException  e) {
            throw new DaoException("Inserting the course to the database failed.", e);
        }
    }
}
