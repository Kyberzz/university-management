package ua.com.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.exception.ServiceException;

public interface LessonService extends GenericService<LessonDTO> {
    
    public List<LessonDTO> applyTimetable(LocalDate date, int timetableId);
    
    public void sortByLessonOrder(List<LessonDTO> lessons);
    
    public void addLessonTiming(List<LessonDTO> lessons);
    
    public void addLessonTiming(LessonDTO lesson);
    
    public LocalDate moveForward(LocalDate date);
    
    public LocalDate moveBack(LocalDate date);
    
    public List<List<List<LessonDTO>>> getMonthLessons(LocalDate date) 
            throws ServiceException;
    
    public List<LessonDTO> getDayLessons(LocalDate date) throws ServiceException;
}