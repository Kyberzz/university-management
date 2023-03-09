package ua.com.foxminded.university.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ua.com.foxminded.university.exception.ServiceException;

public interface CourseService<T> {

    public static final String HAS_ROLE_STAFF_OR_ADMIN = "hasRole('STAFF', 'ADMIN')";

    public List<T> getAllCourses() throws ServiceException;
    
    @PreAuthorize(HAS_ROLE_STAFF_OR_ADMIN)
    public void updateCourse(T course) throws ServiceException;

    public T getTimetableListByCourseId(int id) throws ServiceException;
}
