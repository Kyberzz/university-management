package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimetableModel;

public interface TimetableService extends GenericService<TimetableModel> {
    
    public List<TimetableModel> getAllWithTimings() throws ServiceException;
}
