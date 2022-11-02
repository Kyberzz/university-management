package ua.com.foxminded.university.repository;

import ua.com.foxminded.university.entity.CourseEntity;

public interface CourseRepository extends GenericRepository<CourseEntity> {
    
    public CourseEntity getTimetableListByCourseId(int id) throws RepositoryException;
}
