package ua.com.foxminded.university.service;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;

public interface CourseService extends GenericService<CourseModel> {
    
    public CourseModel getTimetableAndTeachersByCourseId(int id) throws ServiceException;
    
    public CourseModel getTimetableListByCourseId(int id) throws ServiceException;
}
