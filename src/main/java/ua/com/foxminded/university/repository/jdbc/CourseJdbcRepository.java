package ua.com.foxminded.university.repository.jdbc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.RepositoryException;

@Slf4j
@Repository
public class CourseJdbcRepository implements CourseRepository {
    
    private static final String SELECT_TIMETABLE_LIST = "select c from CourseEntity c "
            + "join fetch c.timetableList where c.id = :id";
    private static final String COURSE_ID_COLUMN_NAME = "id";
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public CourseJdbcRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Override
    public CourseEntity getTimetableListByCourseId(int id) throws RepositoryException {
        log.debug("Get timetable list by course id={}", id);
        
        try {
            CourseEntity course = entityManager.createQuery(SELECT_TIMETABLE_LIST, CourseEntity.class)
                                               .setParameter(COURSE_ID_COLUMN_NAME, id)
                                               .getSingleResult();
            log.trace("Timetable list of course with id={} was received.", course.getId());
            return course;
        } catch (IllegalStateException e) {
            throw new RepositoryException("Getting the timetable list by course id failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws RepositoryException {
        log.debug("Delete course by id={}", id);
        
        try {
            CourseEntity course = entityManager.find(CourseEntity.class, id);
            entityManager.remove(course);
            log.trace("Course with id={} was deleted.", id);
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Deleting the course by its id failed.", e);
        }
    }
    
    @Override
    public CourseEntity update(CourseEntity entity) throws RepositoryException {
        log.debug("Update course with id={}", entity.getId());
        
        try {
            CourseEntity mergedEntity = entityManager.merge(entity);
            log.trace("Course with id ={} was updated.", entity.getId());
            return mergedEntity;
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Updating the course data failed.", e);
        }
    }
    
    @Override
    public CourseEntity getById(int id) throws RepositoryException {
        log.debug("Get course by id={}", id);
        
        try {
            CourseEntity courseEntity = entityManager.find(CourseEntity.class, id);
            log.trace("Course with id={} was received.", courseEntity.getId());
            return courseEntity;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Getting the course by its id failed.", e);
        }
    }
    
    @Override
    public CourseEntity insert(CourseEntity entity) throws RepositoryException {
        log.debug("Insert course with id={}", entity.getId());
       
        try {
            entityManager.persist(entity);
            log.trace("Course with id={} was inserted.", entity.getId());
            return entity;
        } catch (IllegalArgumentException | EntityExistsException | TransactionRequiredException e) {
            throw new RepositoryException("Inserting the course to the database failed.", e);
        }
    }
}
