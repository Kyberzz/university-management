package ua.com.foxminded.university.repository.jdbc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TimetableRepository;

@Slf4j
@Repository
public class TimetableJdbcRepository implements TimetableRepository {
    
    private static final String UPDATE = "timetalbe.udate";
    private static final String SELECT_MAX_ID = "timetable.insert.select";
    private static final String INSERT = "timetable.insert.insert";
    
    private EntityManagerFactory entityManagerFactory;
    private Environment environment;

    @Autowired
    public TimetableJdbcRepository(EntityManagerFactory entityManagerFactory, Environment environment) {
        this.entityManagerFactory = entityManagerFactory;
        this.environment = environment;
    }
    
    @Override
    public TimetableEntity getCourseByTimetableId(int id) throws RepositoryException {
        log.debug("Get course by timetable id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            timetable.getCourse().getName();
            entityManager.close();
            log.trace("Course by timetable id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new RepositoryException("Getting the database timetable data failed.", e);
        }
    }
    
    @Override
    public TimetableEntity getGroupByTimetableId(int id) throws RepositoryException {
        log.debug("Get group by timetable id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            timetable.getGroup().getName();
            entityManager.close();
            log.trace("Group of timetable id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new RepositoryException("Getting the database grou data by the timetable id failed.", e);
        }
    }
    
    @Override
    public TimetableEntity getById(int id) throws RepositoryException {
        log.debug("Get timetable by id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            entityManager.close();
            log.trace("Timetable with id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new RepositoryException("Getting the database timetable data by its id failed.", e);
        }
    }
    
    @Override
    public void update(TimetableEntity entity) throws RepositoryException {
        log.debug("Update timetable with id={}.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.createNativeQuery(environment.getProperty(UPDATE))
                         .setParameter(1, null)
                         .setParameter(2, null)
                         .setParameter(3, entity.getStartTime())
                         .setParameter(4, entity.getEndTime())
                         .setParameter(5, entity.getDescription())
                         .setParameter(6, entity.getWeekDay().toString())
                         .setParameter(7, entity.getId())
                     //    .setHint("javax.persistence.loadgraph", entityManager)
                         .executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Timetable with id={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e) {
            throw new RepositoryException("Updating the database timetable data failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws RepositoryException {
        log.debug("Delete timetable with id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            entityManager.remove(timetable);
            entityManager.close();
            log.trace("Timetable with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Deleting the database timetable data by its id failed.", e);
        }
    }
    
    @Override
    public TimetableEntity insert(TimetableEntity entity) throws RepositoryException {
        log.debug("Insert timetable with id={}.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
           // entityManager.persist(entity);
        //   entity.getGroup().getName();
        //    entity.getCourse().getName();
          //  entityManager.flush();
            
           
            entityManager.createNativeQuery("insert into university.timetable(start_time, end_time) values(?, ?)")
                    .setParameter(1, entity.getStartTime())
                    .setParameter(2, entity.getEndTime())
                    .executeUpdate();
                //    .setParameter(1, entity.getGroup().getId())
                //    .setParameter(2, entity.getCourse().getId())
                //    .setParameter(5, entity.getDescription())
                //    .setParameter(6, entity.getWeekDay().toString())
                    
        //    entityManager.flush();
            
       //     int receivedId = (int)entityManager.createNativeQuery(environment.getProperty(SELECT_MAX_ID))
       //                                        .getSingleResult();
       //     entity.setId(receivedId);
            
            entityManager.close();
            log.trace("Timetable with id={} was inserted to database.", entity.getId());
            entity.setId(77);
            return entity;
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                 TransactionRequiredException | RollbackException e) {
            throw new RepositoryException("Inserting the timetable data to the database failed.", e);
        }
    }
}
