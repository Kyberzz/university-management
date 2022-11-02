package ua.com.foxminded.university.repository;

import ua.com.foxminded.university.entity.StudentEntity;

public interface StudentRepository extends GenericRepository<StudentEntity> {
    
    public StudentEntity getGroupByStudentId(int id) throws RepositoryException;
}
