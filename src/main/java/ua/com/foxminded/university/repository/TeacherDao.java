package ua.com.foxminded.university.repository;

import ua.com.foxminded.university.entity.TeacherEntity;

public interface TeacherDao extends GenericDao<TeacherEntity> {
    
    public TeacherEntity getCourseListByTeacherId(int id) throws DaoException;
}
