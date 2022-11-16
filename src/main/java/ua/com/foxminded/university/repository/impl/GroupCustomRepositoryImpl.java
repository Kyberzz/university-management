package ua.com.foxminded.university.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.repository.GroupCustomRepository;
import ua.com.foxminded.university.repository.RepositoryException;

@Slf4j
public class GroupCustomRepositoryImpl implements GroupCustomRepository<GroupEntity> {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public GroupEntity findById(int id) throws RepositoryException {
        try {
            log.debug("Find a goup by id={}", id);
            GroupEntity group = entityManager.find(GroupEntity.class, id);
            log.trace("A group with id={} was retrieved", id);
            return group;
        } catch(IllegalArgumentException e) {
            throw new RepositoryException("Finding a group with id={} fails", e);
        }
    }
}
