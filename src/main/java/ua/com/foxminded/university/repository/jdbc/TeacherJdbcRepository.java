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
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TeacherRepository;

@Slf4j
@Repository
public class TeacherJdbcRepository implements TeacherRepository {
    
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public TeacherJdbcRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public TeacherEntity getCourseListByTeacherId(int id) throws RepositoryException {
        log.debug("Get courses list by teacher id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            EntityGraph<TeacherEntity> entityGraph = entityManager.createEntityGraph(TeacherEntity.class);
            entityGraph.addAttributeNodes("id", "firstName", "lastName", "courseList");
            Map<String, Object> properties = new HashMap<>();
            properties.put("javax.persistence.fetchgraph", entityGraph);
            TeacherEntity teacher = entityManager.find(TeacherEntity.class, id, properties);
            teacher.getCourseList();
            entityManager.close();
            log.trace("Courses list of teacher id={} was received");
            return teacher;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new RepositoryException("Getting the course list data by the teacher id from the database failed.",
                                   e);
        }
    }
    
    @Override
    public void deleteById(int id) throws RepositoryException {
        log.debug("Delete teacher with id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TeacherEntity teacher = entityManager.find(TeacherEntity.class, id);
            entityManager.remove(teacher);
            entityManager.close();
            log.trace("Teacher with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException 
                e) {
            throw new RepositoryException("Deleting the database teacher data failed.", e);
        }
    }
    
    @Override
    public void update(TeacherEntity entity) throws RepositoryException {
        log.debug("Udate teacher with id={}.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Teacher with id={} was updated.", entity.getId());
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException | 
                 RollbackException e){
            throw new RepositoryException("Updating the database teacher data failed.", e);
        }
    }
    
    @Override
    public TeacherEntity getById(int id) throws RepositoryException {
        log.debug("Get teacher with id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TeacherEntity teacher = entityManager.find(TeacherEntity.class, id);
            entityManager.close();
            log.trace("Teacher with id={} was received.", teacher.getId());
            return teacher;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new RepositoryException("Getting the database teacher data failed.", e);
        }
    }
    
    @Override
    public TeacherEntity insert(TeacherEntity entity) throws RepositoryException {
        log.debug("Insert teacher with id={} to the database.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Teacher with id={} was added to the database.", entity.getId());
            return entity;
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                 TransactionRequiredException | RollbackException e) {
            throw new RepositoryException("Inserting the database teacher data failed.", e);
        }
    }
}
