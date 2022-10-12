package ua.com.foxminded.university.dao.jdbc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.TeacherEntity;

@Slf4j
@Repository
public class TeacherJdbcDao implements TeacherDao {
    
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    public TeacherJdbcDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public TeacherEntity getCourseListByTeacherId(int id) throws DaoException {
        log.debug("Get courses list by teacher id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TeacherEntity teacher = entityManager.find(TeacherEntity.class, id);
            teacher.getCourseList().iterator();
            entityManager.close();
            log.trace("Courses list of teacher id={} was received");
            return teacher;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the course list data by the teacher id from the database failed.",
                                   e);
        }
    }
    
    @Override
    public void deleteById(int id) throws DaoException {
        log.debug("Delete teacher with id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TeacherEntity teacher = new TeacherEntity();
            teacher.setId(id);
            entityManager.remove(teacher);
            entityManager.close();
            log.trace("Teacher with id={} was deleted.", id);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException 
                e) {
            throw new DaoException("Deleting the database teacher data failed.", e);
        }
    }
    
    @Transactional
    @Override
    public void update(TeacherEntity entity) throws DaoException {
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
            throw new DaoException("Updating the database teacher data failed.", e);
        }
    }
    
    @Override
    public TeacherEntity getById(int id) throws DaoException {
        log.debug("Get teacher with id={}.", id);
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TeacherEntity teacher = entityManager.find(TeacherEntity.class, id);
            entityManager.close();
            log.trace("Teacher with id={} was received.", teacher.getId());
            return teacher;
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DaoException("Getting the database teacher data failed.", e);
        }
    }
    
    @Transactional
    @Override
    public TeacherEntity insert(TeacherEntity entity) throws DaoException {
        log.debug("Insert teacher with id={} to the database.", entity.getId());
        
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.refresh(entity);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.trace("Teacher with id={} was added to the database.", entity.getId());
            return entity;
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | 
                 TransactionRequiredException | RollbackException e) {
            throw new DaoException("Inserting the database teacher data failed.", e);
        }
    }
}
