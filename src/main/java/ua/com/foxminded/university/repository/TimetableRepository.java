package ua.com.foxminded.university.repository;

import ua.com.foxminded.university.entity.TimetableEntity;

public interface TimetableRepository extends GenericRepository<TimetableEntity> {
    
    public TimetableEntity getCourseByTimetableId(int id) throws RepositoryException;
    public TimetableEntity getGroupByTimetableId(int id) throws RepositoryException;
}
