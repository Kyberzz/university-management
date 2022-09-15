package ua.com.foxminded.university.service;

public interface TimetableService<T> {
    
    public int updateTimetable(T model) throws ServiceException;
}
