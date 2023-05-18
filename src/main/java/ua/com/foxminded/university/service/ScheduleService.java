package ua.com.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.ScheduleModel;

public interface ScheduleService extends GenericService<ScheduleModel> {
    
    public LocalDate moveForward(LocalDate date);
    
    public LocalDate moveBack(LocalDate date);
    
    public List<List<List<ScheduleModel>>> getMonthSchedule(LocalDate date) 
            throws ServiceException;
    
    public List<ScheduleModel> getDaySdhedule(LocalDate date) throws ServiceException;
}