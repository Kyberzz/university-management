package ua.com.foxminded.university.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.TimetableService;

@Service
public class TimetableServiceImpl implements TimetableService<TimetableModel> {
    
    private static final Logger logger = LoggerFactory.getLogger(TimetableServiceImpl.class);
    
    private TimetableDao timetableDao;
    
    @Autowired
    public TimetableServiceImpl(TimetableDao timetableDao) {
        this.timetableDao = timetableDao;
    }
    
    @Override
    public int updateTimetable(TimetableModel timetableModel) throws ServiceException {
        TimetableEntity timetableEntity = new TimetableEntity();
        timetableEntity.setCourse(new CourseEntity(timetableModel.getId()));
        timetableEntity.setDescription(timetableModel.getDescription());
        timetableEntity.setEndTime(timetableModel.getEndTime());
        timetableEntity.setGroup(new GroupEntity(timetableModel.getGroup().getId()));
        timetableEntity.setId(timetableModel.getId());
        timetableEntity.setStartTime(timetableModel.getStartTime());
        timetableEntity.setWeekDay(WeekDayEntity.valueOf(timetableModel.getWeekDay().toString()));
        
        int updatedTimetablesQuantity = 0;
        
        try {
            updatedTimetablesQuantity = timetableDao.update(timetableEntity);
        } catch (DaoException e) {
            String errorMessage = "Updating the timetable failed.";
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
        return updatedTimetablesQuantity;
    }
}
