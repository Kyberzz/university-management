package ua.com.foxminded.university.repository.jdbc;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TimetableRepository;

@Slf4j
@Repository
public class TimetableJdbcRepository implements TimetableRepository {
    
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
            EntityGraph<TimetableEntity> entityGraph = entityManager.createEntityGraph(TimetableEntity.class);
            entityGraph.addAttributeNodes("id", "startTime", "description", "course", "weekDay");
            Map<String, Object> properties = new HashMap<>();
            properties.put("javax.persistence.fetchgraph", entityGraph);
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id, properties);
            timetable.getCourse().getId();
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
            EntityGraph<TimetableEntity> entityGraph = entityManager.createEntityGraph(TimetableEntity.class);
            entityGraph.addAttributeNodes("id", "startTime", "endTime", "description", "group", "weekDay");
            Map<String, Object> properties = new HashMap<>();
            properties.put("javax.persistence.fetchgraph", entityGraph);
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id, properties);
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
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
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
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery(environment.getProperty(INSERT))
                    .setParameter(1, entity.getGroup().getId())
                    .setParameter(2, entity.getCourse().getId())
                    .setParameter(3, entity.getStartTime())
                    .setParameter(4, entity.getEndTime())
                    .setParameter(5, entity.getDescription())
                    .setParameter(6, entity.getWeekDay().toString())
                    .executeUpdate();
            entityManager.flush();
            int receivedId = (int)entityManager.createNativeQuery(environment.getProperty(SELECT_MAX_ID))
                                               .getSingleResult();
            entity.setId(receivedId);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Timetable with id={} was inserted to database.", entity.getId());
            return entity;
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                 TransactionRequiredException | RollbackException e) {
            throw new RepositoryException("Inserting the timetable data to the database failed.", e);
        }
    }
}
