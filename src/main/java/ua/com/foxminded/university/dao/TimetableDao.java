package ua.com.foxminded.university.dao;

import java.util.List;

import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;

public interface TimetableDao extends GenericDao<TimetableEntity> {
    
    public int createTimetable(int groupId, int courseId, long startTime, long endTime, 
                               String description, WeekDayEntity weekDay);
    public List<TimetableEntity> getTimetableByStudentId(int id);
    public List<TimetableEntity> getTimetableByTeacherId(int id);
}
