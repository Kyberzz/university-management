package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.model.WeekDayModel;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.ServiceException;

@Service
public class CourseServiceImpl implements CourseService<CourseModel> {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    
    private CourseDao courseDao;
    
    @Autowired
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }
    
    @Override
    public int updateCourse(CourseModel courseModel) throws ServiceException {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setDescription(courseModel.getDescription());
        courseEntity.setId(courseModel.getId());
        courseEntity.setName(courseModel.getName());
        courseEntity.setTeacher(new TeacherEntity(courseModel.getTeacher().getId()));
        
        try {
            int udatedCoursesQuantity = courseDao.update(courseEntity);
            return udatedCoursesQuantity;
        } catch (DaoException e) {
            String errorMessage = "Updating the course failed.";
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }
   
    @Override
    public CourseModel getTimetableListByCourseId(int id) throws ServiceException {
        CourseEntity courseEntityTimetablesList = null;
        
        try {
            courseEntityTimetablesList = courseDao.getTimetableListByCourseId(id);
        } catch (DaoException e) {
            String errorMessage = "Getting timetable list of course id faled.";
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
       
        CourseModel courseModelTimetablesList = new CourseModel();
        List<TimetableModel> timetableList = courseEntityTimetablesList.getTimetableList()
                .stream()
                .map(entity -> {
                    TimetableModel model = new TimetableModel();
                    model.setCourse(new CourseModel(entity.getCourse().getId()));
                    model.setDescription(entity.getDescription());
                    model.setEndTime(entity.getEndTime());
                    model.setGroup(new GroupModel(entity.getGroup().getId()));
                    model.setId(entity.getId());
                    model.setStartTime(entity.getStartTime());
                    model.setWeekDay(WeekDayModel.valueOf(entity.getWeekDay().toString()));
                    return model;
                }).collect(Collectors.toList());
        
        courseModelTimetablesList.setTimetableList(timetableList);
        courseModelTimetablesList.setDescription(courseEntityTimetablesList.getDescription());
        courseModelTimetablesList.setId(courseEntityTimetablesList.getId());
        courseModelTimetablesList.setName(courseEntityTimetablesList.getName());
        courseModelTimetablesList.setTeacher(new TeacherModel(courseEntityTimetablesList.getTeacher()
                                                                                      .getId()));
        return courseModelTimetablesList;
    }
}
