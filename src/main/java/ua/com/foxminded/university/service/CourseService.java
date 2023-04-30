package ua.com.foxminded.university.service;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;

public interface CourseService extends GenericService<CourseModel> {
    
    public void deassignTeacherToCourse(int teacherId, int courseId);
    
    public void assignTeacherToCourse(int teacherId, int courseId);
    
    public CourseModel getTimetableAndTeachersByCourseId(int id) throws ServiceException;
    
    public CourseModel getTimetableListByCourseId(int id) throws ServiceException;
}
