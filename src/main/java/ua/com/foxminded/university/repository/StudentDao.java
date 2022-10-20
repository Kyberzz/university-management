package ua.com.foxminded.university.repository;

import ua.com.foxminded.university.entity.StudentEntity;

public interface StudentDao extends GenericDao<StudentEntity> {
    
    public StudentEntity getGroupByStudentId(int id) throws DaoException;
}
