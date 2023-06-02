package ua.com.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.exception.ServiceException;

public interface LessonService extends GenericService<LessonDTO> {
    
    public LocalDate moveWeekBack(LocalDate date);
    
    public LocalDate moveWeekForward(LocalDate date);
    
    public List<List<LessonDTO>> getWeekLessonsOwnedByTeacher(LocalDate date, int teacherId) 
            throws ServiceException;
    
    public List<LessonDTO> applyTimetable(LocalDate date, int timetableId) 
            throws ServiceException;
    
    public void sortByLessonOrder(List<LessonDTO> lessons);
    
    public void addLessonTiming(List<LessonDTO> lessons);
    
    public void addLessonTiming(LessonDTO lesson);
    
    public LocalDate moveMonthForward(LocalDate date);
    
    public LocalDate moveMonthBack(LocalDate date);
    
    public List<List<List<LessonDTO>>> getMonthLessons(LocalDate date) 
            throws ServiceException;
    
    public List<LessonDTO> getDayLessons(LocalDate date) throws ServiceException;
}