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
import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.entity.TimetableEntity;

@Slf4j
@Repository
public class TimetableJdbcDao implements TimetableDao {
    
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public TimetableJdbcDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public TimetableEntity getCourseByTimetableId(int id) throws DaoException {
        log.debug("Get course by timetable id={}.", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            timetable.getCourse().getId();
            log.trace("Course by timetable id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the database timetable data failed.", e);
        }
    }
    
    @Override
    public TimetableEntity getGroupByTimetableId(int id) throws DaoException {
        log.debug("Get group by timetable id={}.", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            log.trace("Group of timetable id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the database grou data by the timetable id failed.", e);
        }
    }
    
    @Override
    public TimetableEntity getById(int id) throws DaoException {
        log.debug("Get timetable by id={}.", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            log.trace("Timetable with id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the database timetable data by its id failed.", e);
        }
    }
    
    @Transactional
    @Override
    public void update(TimetableEntity entity) throws DaoException {
        log.debug("Update timetable with id={}.", entity.getId());
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            log.trace("Timetable with id={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e) {
            throw new DaoException("Updating the database timetable data failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws DaoException {
        log.debug("Delete timetable with id={}.", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            TimetableEntity timetable = new TimetableEntity();
            timetable.setId(id);
            entityManager.remove(timetable);
            log.trace("Timetable with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException e) {
            throw new DaoException("Deleting the database timetable data by its id failed.", e);
        }
    }
    
    @Transactional
    @Override
    public void insert(TimetableEntity entity) throws DaoException {
        log.debug("Insert timetable with id={}.", entity.getId());
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            log.trace("Timetable with id={} was inserted to database.", entity.getId());
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                 TransactionRequiredException | RollbackException e) {
            throw new DaoException("Inserting the timetable data to the database failed.", e);
        }
    }
}
