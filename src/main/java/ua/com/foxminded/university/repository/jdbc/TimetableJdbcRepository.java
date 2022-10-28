package ua.com.foxminded.university.repository.jdbc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TimetableRepository;

@Slf4j
@Repository
public class TimetableJdbcRepository implements TimetableRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public TimetableJdbcRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public TimetableEntity getCourseByTimetableId(int id) throws RepositoryException {
        log.debug("Get course by timetable id={}.", id);
        
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TimetableEntity> criteriaQuery = criteriaBuilder.createQuery(TimetableEntity.class);
            Root<TimetableEntity> rootTimetable = criteriaQuery.from(TimetableEntity.class);
            rootTimetable.fetch("course", JoinType.INNER);
            criteriaQuery.select(rootTimetable);
            criteriaQuery.where(criteriaBuilder.equal(rootTimetable.get("id"), id));
            TimetableEntity timetable = entityManager.createQuery(criteriaQuery).getSingleResult();
            log.trace("Course by timetable id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Getting the database timetable data failed.", e);
        }
    }
    
    @Override
    public TimetableEntity getGroupByTimetableId(int id) throws RepositoryException {
        log.debug("Get group by timetable id={}.", id);
        
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TimetableEntity> criteriaQuery = criteriaBuilder.createQuery(TimetableEntity.class);
            Root<TimetableEntity> rootTimetable = criteriaQuery.from(TimetableEntity.class);
            rootTimetable.fetch("group", JoinType.INNER);
            criteriaQuery.select(rootTimetable);
            criteriaQuery.where(criteriaBuilder.equal(rootTimetable.get("id"), id));
            TimetableEntity timetable = entityManager.createQuery(criteriaQuery).getSingleResult();
            log.trace("Group of timetable id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Getting the database grou data by the timetable id failed.", e);
        }
    }
    
    @Override
    public TimetableEntity getById(int id) throws RepositoryException {
        log.debug("Get timetable by id={}.", id);
        
        try {
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            log.trace("Timetable with id={} was received.", timetable.getId());
            return timetable;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Getting the database timetable data by its id failed.", e);
        }
    }
    
    @Override
    public TimetableEntity update(TimetableEntity entity) throws RepositoryException {
        log.debug("Update timetable with id={}.", entity.getId());
        
        try {
            TimetableEntity mergedEntity = entityManager.merge(entity);
            log.trace("Timetable with id={} was updated.", entity.getId());
            return mergedEntity;
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Updating the database timetable data failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws RepositoryException {
        log.debug("Delete timetable with id={}.", id);
        
        try {
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            entityManager.remove(timetable);
            log.trace("Timetable with id={} was deleted.", id);
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Deleting the database timetable data by its id failed.", e);
        }
    }
    
    @Override
    public TimetableEntity insert(TimetableEntity entity) throws RepositoryException {
        log.debug("Insert timetable with id={}.", entity.getId());
        
        try {
            entityManager.persist(entity);
            log.trace("Timetable with id={} was inserted to database.", entity.getId());
            return entity;
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Inserting the timetable data to the database failed.", e);
        }
    }
}
