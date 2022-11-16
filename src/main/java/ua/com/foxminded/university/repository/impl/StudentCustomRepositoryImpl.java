package ua.com.foxminded.university.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.StudentCustomRepository;

@Slf4j
public class StudentCustomRepositoryImpl implements StudentCustomRepository<StudentEntity> {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public StudentCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public StudentEntity findById(int id) throws RepositoryException {
        try {
            log.debug("Find student by id={}", id);
            StudentEntity student = entityManager.find(StudentEntity.class, id);
            log.trace("A student with id={} was retrieved", StudentEntity.class.toString(), id);
            return student;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Find the student failed", e);
        }
    }
}
