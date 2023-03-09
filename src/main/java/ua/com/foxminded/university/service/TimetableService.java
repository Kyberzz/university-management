package ua.com.foxminded.university.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ua.com.foxminded.university.exception.ServiceException;

public interface TimetableService<T> {

    public static final String HAS_ROLE_STAFF_OR_ADMIN = "hasAnyRole('STAFF', 'ADMIN')";
    
    public List<T> getAllTimetables() throws ServiceException;
    
    @PreAuthorize(HAS_ROLE_STAFF_OR_ADMIN)
    public void updateTimetable(T model) throws ServiceException;
}
