package ua.com.foxminded.university.repository.jdbc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.GroupRepository;

@Slf4j
@Repository
public class GroupJdbcRepository implements GroupRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public GroupJdbcRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public GroupEntity getById(int id) throws RepositoryException {
        log.debug("Get group by id={}", id);
        
        try {
            GroupEntity group = entityManager.find(GroupEntity.class, id);
            log.trace("Group with id={} was received.", group.getId());
            return group;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Getting the group by its id failed.", e);
        }
    }
    
    @Override  
    public GroupEntity getTimetableListByGroupId(int id) throws RepositoryException {
        log.debug("Get timetable list by group id={}", id);
        
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<GroupEntity> query = criteriaBuilder.createQuery(GroupEntity.class);
            Root<GroupEntity> rootGroup = query.from(GroupEntity.class);
            rootGroup.fetch("timetableList");
            query.select(rootGroup);
            query.where(criteriaBuilder.equal(rootGroup.get("id"), id));
            GroupEntity group = entityManager.createQuery(query).getSingleResult();
            
            log.trace("Timetable list of group with id={} was received.", group.getId());
            return group;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Getting timetable list by the group id failed.", e);
        }
    }
    
    @Override
    public GroupEntity getStudentListByGroupId(int id) throws RepositoryException {
        log.debug("Get students list by group id={}", id);
        
        try {
            GroupEntity group = entityManager.createQuery("select g "
                                                        + "from GroupEntity g "
                                                        + "join fetch g.studentList "
                                                        + "where g.id = :id", GroupEntity.class)
                                             .setParameter("id", id)
                                             .getSingleResult();
            log.trace("Students list of the group with id={} was received", group.getId());
            return group;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Getting students list by the group id failed.", e);
        }
    }
    
   
    @Override
    public GroupEntity insert(GroupEntity entity) throws RepositoryException {
        log.debug("Insert group with id={}", entity.getId());
        
        try {
            entityManager.persist(entity);
            log.trace("Group with id={} was inserted.", entity.getId());
            return entity;
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Inserting the group to the database failed.", e);
        }
    }
    
    @Override
    public GroupEntity update(GroupEntity entity) throws RepositoryException {
        log.debug("Update group with id={}.", entity.getId());
        
        try {
            GroupEntity mergedEntity = entityManager.merge(entity);
            log.trace("Group with id={} was updated.", entity.getId());
            return mergedEntity;
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Updating the group failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws RepositoryException {
        log.debug("Delete group by id={}.", id);
        
        try {
            GroupEntity group = entityManager.find(GroupEntity.class, id);
            entityManager.remove(group);
            log.trace("Group with id={} was deleted.", id);
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Deleting the group by its id failed.", e);
        }
    }
}
