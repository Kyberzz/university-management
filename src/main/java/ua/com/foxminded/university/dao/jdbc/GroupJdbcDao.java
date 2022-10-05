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
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.entity.GroupEntity;

@Slf4j
@Repository
public class GroupJdbcDao implements GroupDao {
    
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public GroupJdbcDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public GroupEntity getById(int id) throws DaoException {
        log.debug("Get group by id={}", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            GroupEntity group = entityManager.find(GroupEntity.class, id);
            log.trace("Group with id={} was received.", group.getId());
            return group;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the group by its id failed.", e);
        }
    }
    
    @Override  
    public GroupEntity getTimetableListByGroupId(int id) throws DaoException {
        log.debug("Get timetable list by group id={}", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            GroupEntity group = entityManager.find(GroupEntity.class, id);
            group.getTimetableList().iterator();
            log.trace("Timetable list of group with id={} was received.", group.getId());
            return group;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting timetable list by the group id failed.", e);
        }
    }
    
    @Override
    public GroupEntity getStudentListByGroupId(int id) throws DaoException {
        log.debug("Get students list by group id={}", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            GroupEntity group = entityManager.find(GroupEntity.class, id);
            group.getStudentList().iterator();
            log.trace("Students list of the group with id={} was received", group.getId());
            return group;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting students list by the group id failed.", e);
        }
    }
    
    @Override
    public void insert(GroupEntity entity) throws DaoException {
        log.debug("Insert group with id={}", entity.getId());
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            log.trace("Group with id={} was inserted.", entity.getId());
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                 TransactionRequiredException | RollbackException e) {
            throw new DaoException("Inserting the group to the database failed.", e);
        }
    }
    
    @Override
    @Transactional
    public void update(GroupEntity entity) throws DaoException {
        log.debug("Update group with id={}.", entity.getId());
        
        try (var entityManager = entityManagerFactory.createEntityManager();){
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            log.trace("Group with id={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e) {
            throw new DaoException("Updating the group failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws DaoException {
        log.debug("Delete group by id={}.", id);
        
        try (var entityManager = entityManagerFactory.createEntityManager();) {
            GroupEntity group = new GroupEntity();
            group.setId(id);
            entityManager.remove(group);
            log.trace("Group with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException e) {
            throw new DaoException("Deleting the group by its id failed.", e);
        }
    }
}
