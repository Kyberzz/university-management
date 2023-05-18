package ua.com.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.ScheduleModel;

public interface ScheduleService extends GenericService<ScheduleModel> {
    
    public LocalDate moveForward(LocalDate date);
    
    public LocalDate moveBack(LocalDate date);
    
    public List<List<List<ScheduleModel>>> getMonthTimetable(LocalDate date) 
            throws ServiceException;
    
    public List<ScheduleModel> getDayTimetalbe(LocalDate date) throws ServiceException;
}