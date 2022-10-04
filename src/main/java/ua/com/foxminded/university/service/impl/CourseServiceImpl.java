package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class CourseServiceImpl implements CourseService<CourseModel> {
    
    private CourseDao courseDao;
    
    @Autowired
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }
    
    @Override
    public void updateCourse(CourseModel courseModel) throws ServiceException {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(courseModel.getId());
        courseEntity.setDescription(courseModel.getDescription());
        courseEntity.setName(courseModel.getName());
        TeacherEntity teacher = new TeacherEntity();
        teacher.setId(courseModel.getTeacher().getId());
        courseEntity.setTeacher(teacher);
        
        try {
            courseDao.update(courseEntity);
        } catch (DaoException e) {
            throw new ServiceException("Updating the course failed.", e);
        }
    }
   
    @Override
    public CourseModel getTimetableListByCourseId(int id) throws ServiceException {
        CourseEntity courseEntityTimetablesList = null;
        
        try {
            courseEntityTimetablesList = courseDao.getTimetableListByCourseId(id);
        } catch (DaoException e) {
            throw new ServiceException("Getting timetable list of course id faled.", e);
        }
        
        List<TimetableModel> timetableList = courseEntityTimetablesList.getTimetableList()
                .stream()
                .map(entity -> {
                    TimetableModel model = new TimetableModel(entity.getId());
                    model.setCourse(new CourseModel(entity.getCourse().getId()));
                    model.setDescription(entity.getDescription());
                    model.setEndTime(entity.getEndTime());
                    model.setGroup(new GroupModel(entity.getGroup().getId()));
                    model.setStartTime(entity.getStartTime());
                    model.setWeekDay(WeekDayModel.valueOf(entity.getWeekDay().toString()));
                    return model;
                }).collect(Collectors.toList());
        CourseModel courseModelTimetablesList = new CourseModel(courseEntityTimetablesList.getId());
        courseModelTimetablesList.setTimetableList(timetableList);
        courseModelTimetablesList.setDescription(courseEntityTimetablesList.getDescription());
        courseModelTimetablesList.setName(courseEntityTimetablesList.getName());
        courseModelTimetablesList.setTeacher(new TeacherModel(courseEntityTimetablesList.getTeacher()
                                                                                      .getId()));
        return courseModelTimetablesList;
    }
}
