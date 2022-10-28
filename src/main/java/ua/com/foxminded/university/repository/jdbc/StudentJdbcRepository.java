package ua.com.foxminded.university.repository.jdbc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.StudentRepository;

@Slf4j
@Repository
public class StudentJdbcRepository implements StudentRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public StudentJdbcRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public StudentEntity getGroupByStudentId(int id) throws RepositoryException {
        log.debug("Get group by student id={}.", id);
        
        try {
            StudentEntity student = entityManager.createQuery(
                    "select s "
                  + "from StudentEntity s "
                  + "left join fetch s.group "
                  + "where s.id = :id", StudentEntity.class)
            .setParameter("id", id)
            .getSingleResult();
            log.trace("Group having student id={} was received.", student.getId());
            return student;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Getting group by the student id failed.", e);
        }
    }
    
    @Override
    public StudentEntity insert(StudentEntity entity) throws RepositoryException {
        log.debug("Insert student with id={}.", entity.getId());
        
        try {
            entityManager.persist(entity);
            log.trace("Student with id={} was inserted.", entity.getId());
            return entity;
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Inserting the student to the database failed.", e);
        }
    }
    
    @Override
    public StudentEntity getById(int id) throws RepositoryException {
        log.debug("Get student by id={}.", id);
        
        try {
            StudentEntity student = entityManager.find(StudentEntity.class, id);
            log.trace("Student with id={} was received.", student.getId());
            return student;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Getting the student by its id failed.", e);
        }
    }
    
    @Override
    public StudentEntity update(StudentEntity entity) throws RepositoryException {
        log.debug("Update student with id={}.", entity.getId());
        
        try {
            StudentEntity mergedEntity = entityManager.merge(entity);
            log.debug("Student with id={} was updated.", entity.getId());
            return mergedEntity;
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Updating the student data failed.", e);
        }
    }

    @Override
    public void deleteById(int id) throws RepositoryException {
        log.debug("Delete student by id={}.", id);
        
        try {
            StudentEntity student = entityManager.find(StudentEntity.class, id) ;
            entityManager.remove(student);
            log.trace("Student with id={} was deleted.", id);
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Deleting the student data failed.", e);
        }
    }
}
