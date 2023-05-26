package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.TimetableDTO;
import ua.com.foxminded.university.exception.ServiceException;

public interface TimetableService extends GenericService<TimetableDTO> {
    
    public void sortByName(List<TimetableDTO> timetables);
    
    public void sortTimingsByStartTime(TimetableDTO timetable);
    
    public TimetableDTO getByIdWithTimings(int id) throws ServiceException;
    
    public List<TimetableDTO> getAllWithTimings() throws ServiceException;
}
