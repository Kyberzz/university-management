package ua.com.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimetableModel;

public interface TimetableService extends GenericService<TimetableModel> {
    
    public void moveBackDatestamp(TimetableModel timetable);
    
    public void moveForwardDatestamp(TimetableModel timetable);
    
    public List<List<List<TimetableModel>>> getMonthTimetable(LocalDate date) 
            throws ServiceException;
    
    public List<List<TimetableModel>> getWeekTimetable(LocalDate date) 
            throws ServiceException;
    
    public List<TimetableModel> getDayTimetalbe(LocalDate date) throws ServiceException;
}
