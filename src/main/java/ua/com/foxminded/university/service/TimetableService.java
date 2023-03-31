package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimetableModel;

public interface TimetableService {

    public List<TimetableModel> getAll() throws ServiceException;
    
    public void updateTimetable(TimetableModel timetable) throws ServiceException;
}
