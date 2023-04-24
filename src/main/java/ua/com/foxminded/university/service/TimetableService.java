package ua.com.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimetableModel;

public interface TimetableService {
    
    public List<List<List<TimetableModel>>> getMonthTimetables(LocalDate date) 
            throws ServiceException;
    
    public List<List<TimetableModel>> getWeekTimetables(LocalDate date) 
            throws ServiceException;
    
    public List<TimetableModel> getDayTimetalbes(LocalDate date) throws ServiceException;

    public List<TimetableModel> getAll() throws ServiceException;
    
    public void updateTimetable(TimetableModel timetable) throws ServiceException;
}
