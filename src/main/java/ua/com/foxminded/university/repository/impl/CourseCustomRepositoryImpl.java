package ua.com.foxminded.university.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.repository.CourseCustomRepository;
import ua.com.foxminded.university.repository.RepositoryException;

@Slf4j
public class CourseCustomRepositoryImpl implements CourseCustomRepository<CourseEntity> {
    
    @PersistenceContext
    private EntityManager entityManager;

    public CourseCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public CourseEntity findById(int id) throws RepositoryException {
        log.debug("Find course by id={}", id);
        CourseEntity course = entityManager.find(CourseEntity.class, id);
        log.trace("A course with id={} was retrieved", id);
        return course;
    }
}
