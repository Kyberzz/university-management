package ua.com.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import ua.com.foxminded.university.dto.LessonDTO;

public interface LessonService extends GenericService<LessonDTO> {
    
    public Set<LessonDTO> sortByDatestamp(Set<LessonDTO> lessons);
    
    public LocalDate moveWeekForward(LocalDate date);
    
    public LocalDate moveWeekBack(LocalDate date);
    
    public List<List<LessonDTO>> getWeekLessonsOwnedByTeacher(LocalDate date, int teacherId);
    
    public List<LessonDTO> applyTimetable(LocalDate date, int timetableId);
    
    public void sortByLessonOrder(List<LessonDTO> lessons);
    
    public void addLessonTiming(List<LessonDTO> lessons);
    
    public void addLessonTiming(LessonDTO lesson);
    
    public LocalDate moveMonthForward(LocalDate date);
    
    public LocalDate moveMonthBack(LocalDate date);
    
    public List<List<List<LessonDTO>>> getMonthLessons(LocalDate date);
    
    public List<LessonDTO> getDayLessons(LocalDate date);
}