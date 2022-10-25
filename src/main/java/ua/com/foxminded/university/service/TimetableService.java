package ua.com.foxminded.university.service;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TimetableService<T> {
    
    public void updateTimetable(T model) throws ServiceException;
}
