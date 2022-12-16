package ua.com.foxminded.university.buisness.model.service;

public interface TimetableService<T> {
    
    public void updateTimetable(T model) throws ServiceException;
}
