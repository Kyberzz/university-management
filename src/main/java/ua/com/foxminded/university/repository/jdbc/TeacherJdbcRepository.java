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
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TeacherRepository;

@Slf4j
@Repository
public class TeacherJdbcRepository implements TeacherRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public TeacherJdbcRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public TeacherEntity getCourseListByTeacherId(int id) throws RepositoryException {
        log.debug("Get courses list by teacher id={}.", id);
        
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TeacherEntity> criteriaQuery = criteriaBuilder.createQuery(TeacherEntity.class);
            Root<TeacherEntity> rootTeacher = criteriaQuery.from(TeacherEntity.class);
            rootTeacher.fetch("courseList", JoinType.INNER);
            criteriaQuery.select(rootTeacher);
            criteriaQuery.where(criteriaBuilder.equal(rootTeacher.get("id"), id));
            TeacherEntity teacher = entityManager.createQuery(criteriaQuery).getSingleResult();
            log.trace("Courses list of teacher id={} was received");
            return teacher;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Getting the course list data by the teacher "
                                        + "id from the database failed.", e);
        }
    }
    
    @Override
    public void deleteById(int id) throws RepositoryException {
        log.debug("Delete teacher with id={}.", id);
        
        try {
            TeacherEntity teacher = entityManager.find(TeacherEntity.class, id);
            entityManager.remove(teacher);
            log.trace("Teacher with id={} was deleted.", id);
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Deleting the database teacher data failed.", e);
        }
    }
    
    @Override
    public TeacherEntity update(TeacherEntity entity) throws RepositoryException {
        log.debug("Udate teacher with id={}.", entity.getId());
        
        try {
            TeacherEntity mergedEntity = entityManager.merge(entity);
            log.trace("Teacher with id={} was updated.", entity.getId());
            return mergedEntity;
        } catch (IllegalArgumentException | TransactionRequiredException e){
            throw new RepositoryException("Updating the database teacher data failed.", e);
        }
    }
    
    @Override
    public TeacherEntity getById(int id) throws RepositoryException {
        log.debug("Get teacher with id={}.", id);
        
        try {
            TeacherEntity teacher = entityManager.find(TeacherEntity.class, id);
            log.trace("Teacher with id={} was received.", teacher.getId());
            return teacher;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Getting the database teacher data failed.", e);
        }
    }
    
    @Override
    public TeacherEntity insert(TeacherEntity entity) throws RepositoryException {
        log.debug("Insert teacher with id={} to the database.", entity.getId());
        
        try {
            entityManager.persist(entity);
            log.trace("Teacher with id={} was added to the database.", entity.getId());
            return entity;
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            throw new RepositoryException("Inserting the database teacher data failed.", e);
        }
    }
}
