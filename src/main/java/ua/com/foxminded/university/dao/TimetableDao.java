package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.TimetableEntity;

public interface TimetableDao extends GenericDao<TimetableEntity> {
    
    public TimetableEntity getCourseByTimetableId(int id);
    public TimetableEntity getGroupByTimetableId(int id);
}
