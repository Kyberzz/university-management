package ua.com.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimetableModel;

public interface TimetableService extends GenericService<TimetableModel> {
    
    public LocalDate moveForward(LocalDate date);
    
    public LocalDate moveBack(LocalDate date);
    
    public List<List<List<TimetableModel>>> getMonthTimetable(LocalDate date) 
            throws ServiceException;
    
    public List<TimetableModel> getDayTimetalbe(LocalDate date) throws ServiceException;
}