package ua.com.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.LessonModel;

public interface LessonService extends GenericService<LessonModel> {
    
    public LocalDate moveForward(LocalDate date);
    
    public LocalDate moveBack(LocalDate date);
    
    public List<List<List<LessonModel>>> getMonthLessons(LocalDate date) 
            throws ServiceException;
    
    public List<LessonModel> getDayLessons(LocalDate date) throws ServiceException;
}