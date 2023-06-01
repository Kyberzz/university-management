package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.exception.ServiceException;

public interface TeacherService extends GenericService<TeacherDTO> {
    
    public TeacherDTO getTeacherByEmail(String email) throws ServiceException;
    
    public List<TeacherDTO> getByCoursesId(int id) throws ServiceException;
    
    public TeacherDTO getByUserId(int id) throws ServiceException;

    public TeacherDTO getByIdWithCourses(int teacherId) throws ServiceException;
}