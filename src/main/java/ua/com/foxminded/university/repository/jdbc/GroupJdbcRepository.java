package ua.com.foxminded.university.repository.jdbc;


import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.repository.DaoException;
import ua.com.foxminded.university.repository.GroupDao;

@Slf4j
@Repository
public class GroupJdbcRepository implements GroupDao {
    
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public GroupJdbcRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public GroupEntity getById(int id) throws DaoException {
        log.debug("Get group by id={}", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            GroupEntity group = entityManager.find(GroupEntity.class, id);
            entityManager.close();
            log.trace("Group with id={} was received.", group.getId());
            return group;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the group by its id failed.", e);
        }
    }
    
    @Override  
    public GroupEntity getTimetableListByGroupId(int id) throws DaoException {
        log.debug("Get timetable list by group id={}", id);
        
        try  {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            GroupEntity group = entityManager.find(GroupEntity.class, id);
            group.getTimetableList().iterator();
            entityManager.close();
            log.trace("Timetable list of group with id={} was received.", group.getId());
            return group;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting timetable list by the group id failed.", e);
        }
    }
    
    @Override
    public GroupEntity getStudentListByGroupId(int id) throws DaoException {
        log.debug("Get students list by group id={}", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            GroupEntity group = entityManager.find(GroupEntity.class, id);
            group.getStudentList().iterator();
            entityManager.close();
            log.trace("Students list of the group with id={} was received", group.getId());
            return group;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting students list by the group id failed.", e);
        }
    }
    
   
    @Override
    public GroupEntity insert(GroupEntity entity) throws DaoException {
        log.debug("Insert group with id={}", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.refresh(entity);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Group with id={} was inserted.", entity.getId());
            return entity;
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                TransactionRequiredException | RollbackException e) {
            throw new DaoException("Inserting the group to the database failed.", e);
        }
    }
    
    @Override
    public void update(GroupEntity entity) throws DaoException {
        log.debug("Update group with id={}.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Group with id={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e) {
            throw new DaoException("Updating the group failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws DaoException {
        log.debug("Delete group by id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            GroupEntity group = entityManager.find(GroupEntity.class, id);
            entityManager.remove(group);
            entityManager.close();
            log.trace("Group with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException e) {
            throw new DaoException("Deleting the group by its id failed.", e);
        }
    }
}
