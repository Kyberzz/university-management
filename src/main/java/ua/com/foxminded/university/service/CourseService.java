package ua.com.foxminded.university.service;

import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.exception.ServiceException;

public interface CourseService extends GenericService<CourseDTO> {
    
    public void deassignTeacherToCourse(int teacherId, int courseId);
    
    public void assignTeacherToCourse(int teacherId, int courseId);
    
    public CourseDTO getByIdWithLessonsAndTeachers(int courseId) throws ServiceException;
    
    public CourseDTO getByIdWithLessons(int courseId) throws ServiceException;
}