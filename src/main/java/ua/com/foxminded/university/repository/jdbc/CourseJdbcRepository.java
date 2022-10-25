package ua.com.foxminded.university.repository.jdbc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.RepositoryException;

@Slf4j
@Repository
public class CourseJdbcRepository implements CourseRepository {
    
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public CourseJdbcRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public CourseEntity getTimetableListByCourseId(int id) throws RepositoryException {
        log.debug("Get timetable list by course id={}", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            CourseEntity course = entityManager.find(CourseEntity.class, id);
            course.getTimetableList().size();
            entityManager.close();
            log.trace("Timetable list of course with id={} was received.", course.getId());
            return course;
        } catch (IllegalStateException| RollbackException e) {
            throw new RepositoryException("Getting the timetable list by course id failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws RepositoryException {
        log.debug("Delete course by id={}", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            CourseEntity course = entityManager.find(CourseEntity.class, id);
            entityManager.remove(course);
            entityManager.close();
            log.trace("Course with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new RepositoryException("Deleting the course by its id failed.", e);
        }
    }
    
    @Override
    public void update(CourseEntity entity) throws RepositoryException {
        log.debug("Update course with id={}", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            CourseEntity managedEntity = entityManager.find(CourseEntity.class, entity.getId());
            managedEntity.setName("7newCourse");
            entityManager.merge(entity);
            entityManager.close();
            log.trace("Course with id ={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e) {
            throw new RepositoryException("Updating the course data failed.", e);
        }
    }
    
    @Override
    public CourseEntity getById(int id) throws RepositoryException {
        log.debug("Get course by id={}", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            CourseEntity courseEntity = entityManager.find(CourseEntity.class, id);
            entityManager.close();
            log.trace("Course with id={} was received.", courseEntity.getId());
            entityManager.close();
            return courseEntity;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new RepositoryException("Getting the course by its id failed.", e);
        }
    }
    
    @Override
    public CourseEntity insert(CourseEntity entity) throws RepositoryException {
        log.debug("Insert course with id={}", entity.getId());
       
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.persist(entity);
            entityManager.close();
            log.trace("Course with id={} was inserted.", entity.getId());
            return entity;
        } catch (IllegalStateException | IllegalArgumentException | EntityExistsException | 
                 TransactionRequiredException | RollbackException  e) {
            throw new RepositoryException("Inserting the course to the database failed.", e);
        }
    }
}
