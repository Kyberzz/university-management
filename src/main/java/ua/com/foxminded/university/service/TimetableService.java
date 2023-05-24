package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.TimetableDTO;
import ua.com.foxminded.university.exception.ServiceException;

public interface TimetableService extends GenericService<TimetableDTO> {
    
    public TimetableDTO getByIdWithTimings(int id) throws ServiceException;
    
    public List<TimetableDTO> getAllWithTimings() throws ServiceException;
}
