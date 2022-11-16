package ua.com.foxminded.university.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TeacherCustomRepository;

@Slf4j
public class TeacherCustomRepositoryImpl implements TeacherCustomRepository<TeacherEntity> {
    
    @PersistenceContext
    private EntityManager entityManager;

    public TeacherCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public TeacherEntity findById(int id) throws RepositoryException {
        try {
            log.debug("Find teacher by id={}", id);
            TeacherEntity teacher = entityManager.find(TeacherEntity.class, id);
            log.trace("A teacher with id={} was retrieved", id);
            return teacher;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Finding a teacher fails", e);
            
        }
    }
}
