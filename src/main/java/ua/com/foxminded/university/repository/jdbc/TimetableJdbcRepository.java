package ua.com.foxminded.university.repository.jdbc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.repository.DaoException;
import ua.com.foxminded.university.repository.TimetableDao;

@Slf4j
@Repository
public class TimetableJdbcRepository implements TimetableDao {
    
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public TimetableJdbcRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public TimetableEntity getCourseByTimetableId(int id) throws DaoException {
        log.debug("Get course by timetable id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            timetable.getCourse().getId();
            entityManager.close();
            log.trace("Course by timetable id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the database timetable data failed.", e);
        }
    }
    
    @Override
    public TimetableEntity getGroupByTimetableId(int id) throws DaoException {
        log.debug("Get group by timetable id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            entityManager.close();
            log.trace("Group of timetable id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the database grou data by the timetable id failed.", e);
        }
    }
    
    @Override
    public TimetableEntity getById(int id) throws DaoException {
        log.debug("Get timetable by id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            entityManager.close();
            log.trace("Timetable with id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the database timetable data by its id failed.", e);
        }
    }
    
    @Override
    public void update(TimetableEntity entity) throws DaoException {
        log.debug("Update timetable with id={}.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Timetable with id={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e) {
            throw new DaoException("Updating the database timetable data failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws DaoException {
        log.debug("Delete timetable with id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            entityManager.remove(timetable);
            entityManager.close();
            log.trace("Timetable with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException e) {
            throw new DaoException("Deleting the database timetable data by its id failed.", e);
        }
    }
    
    @Override
    public TimetableEntity insert(TimetableEntity entity) throws DaoException {
        log.debug("Insert timetable with id={}.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Timetable with id={} was inserted to database.", entity.getId());
            return entity;
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                 TransactionRequiredException | RollbackException e) {
            throw new DaoException("Inserting the timetable data to the database failed.", e);
        }
    }
}
