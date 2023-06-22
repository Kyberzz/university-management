package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.TimetableDTO;

public interface TimetableService extends GenericService<TimetableDTO> {
    
    public void sortByName(List<TimetableDTO> timetables);
    
    public void sortTimingsByStartTime(TimetableDTO timetable);
    
    public TimetableDTO getByIdWithTimings(int id);
    
    public List<TimetableDTO> getAllWithTimings();
}
