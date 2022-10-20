package ua.com.foxminded.university.repository;

import ua.com.foxminded.university.entity.TimetableEntity;

public interface TimetableDao extends GenericDao<TimetableEntity> {
    
    public TimetableEntity getCourseByTimetableId(int id) throws DaoException;
    public TimetableEntity getGroupByTimetableId(int id) throws DaoException;
}
