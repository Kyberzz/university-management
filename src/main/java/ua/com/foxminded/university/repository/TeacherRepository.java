package ua.com.foxminded.university.repository;

import ua.com.foxminded.university.entity.TeacherEntity;

public interface TeacherRepository extends GenericRepository<TeacherEntity> {
    
    public TeacherEntity getCourseListByTeacherId(int id) throws RepositoryException;
}
