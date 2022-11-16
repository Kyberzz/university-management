package ua.com.foxminded.university.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.repository.TimetableCustomRepository;

@Slf4j
public class TimetableCustomRepositoryImpl implements TimetableCustomRepository<TimetableEntity> {
    
    @PersistenceContext
    private EntityManager entityManager;

    public TimetableCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    

}
