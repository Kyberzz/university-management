package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.univesity.exception.ServiceException;

public interface TimetableService<T> {
    
    public List<T> getAllTimetables() throws ServiceException;
    public void updateTimetable(T model) throws ServiceException;
}
