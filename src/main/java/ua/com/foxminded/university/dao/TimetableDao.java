package ua.com.foxminded.university.dao;

import java.util.List;

import ua.com.foxminded.university.entity.TimetableEntity;

public interface TimetableDao extends GenericDao<TimetableEntity> {
    
    public List<TimetableEntity> getTimetableByStudentId(int id);
    public List<TimetableEntity> getTimetableByTeacherId(int id);
}
