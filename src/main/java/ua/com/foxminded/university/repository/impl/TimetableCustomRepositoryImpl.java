package ua.com.foxminded.university.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TimetableCustomRepository;

@Slf4j
public class TimetableCustomRepositoryImpl implements TimetableCustomRepository<TimetableEntity> {
    
    @PersistenceContext
    private EntityManager entityManager;

    public TimetableCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public TimetableEntity findById(int id) throws RepositoryException {
        try {
            log.debug("Find a timetable by id={}", id);
            TimetableEntity timetable = entityManager.find(TimetableEntity.class, id);
            log.trace("A timetable with id={} was received", id);
            return timetable;
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Finding the timetable fails", e);
        }
    }
}
