package ua.com.foxminded.university.buisness.model.service;

import java.util.List;

public interface TimetableService<T> {
    
    public List<T> getAllTimetables() throws ServiceException;
    public void updateTimetable(T model) throws ServiceException;
}
