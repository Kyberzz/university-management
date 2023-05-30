package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.exception.ServiceException;

public interface TeacherService {
    
    public TeacherDTO getByUserId(int id) throws ServiceException;

    public List<TeacherDTO> getAll() throws ServiceException;

    public TeacherDTO getByIdWithCourses(int teacherId) throws ServiceException;
}