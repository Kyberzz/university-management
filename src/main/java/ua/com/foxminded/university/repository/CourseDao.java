package ua.com.foxminded.university.repository;

import ua.com.foxminded.university.entity.CourseEntity;

public interface CourseDao extends GenericDao<CourseEntity> {
    
    public CourseEntity getTimetableListByCourseId(int id) throws DaoException;
}
