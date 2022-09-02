package ua.com.foxminded.university.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.service.TimetableService;

@Service
public class TimetableServiceImpl implements TimetableService<TimetableModel> {
    
    private TimetableDao timetableDao;
    
    @Autowired
    public TimetableServiceImpl(TimetableDao timetableDao) {
        this.timetableDao = timetableDao;
    }
    
    @Override
    public int updateTimetable(TimetableModel timetableModel) {
        TimetableEntity timetableEntity = new TimetableEntity();
        timetableEntity.setCourse(new CourseEntity(timetableModel.getId()));
        timetableEntity.setDescription(timetableModel.getDescription());
        timetableEntity.setEndTime(timetableModel.getEndTime());
        timetableEntity.setGroup(new GroupEntity(timetableModel.getGroup().getId()));
        timetableEntity.setId(timetableModel.getId());
        timetableEntity.setStartTime(timetableModel.getStartTime());
        timetableEntity.setWeekDay(WeekDayEntity.valueOf(timetableModel.getWeekDay().toString()));
        return timetableDao.update(timetableEntity);
    }
}
