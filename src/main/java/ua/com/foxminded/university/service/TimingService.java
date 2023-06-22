package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.TimingDTO;

public interface TimingService extends GenericService<TimingDTO> {
    
    public List<TimingDTO> getByTimetableId(int id);
}
