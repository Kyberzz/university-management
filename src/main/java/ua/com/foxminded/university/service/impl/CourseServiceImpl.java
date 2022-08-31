package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.model.WeekDayModel;
import ua.com.foxminded.university.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService<CourseModel> {
    
    private CourseDao courseDao;
    
    @Autowired
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }
    
    @Override
    public int updateCourse(CourseModel courseModel) {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setDescription(courseModel.getDescription());
        courseEntity.setId(courseModel.getId());
        courseEntity.setName(courseModel.getName());
        courseEntity.setTeacher(new TeacherEntity(courseModel.getTeacher().getId()));
        return courseDao.update(courseEntity);
    }
   
    @Override
    public CourseModel getTimetableListByCourseId(int id) {
        CourseEntity courseEntityTimetableList = courseDao.getTimetableListByCourseId(id);
        CourseModel courseModelTimetableList = new CourseModel();
        
        List<TimetableModel> timetableList = courseEntityTimetableList.getTimetableList().stream()
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
        
        courseModelTimetableList.setTimetableList(timetableList);
        courseModelTimetableList.setDescription(courseEntityTimetableList.getDescription());
        courseModelTimetableList.setId(courseEntityTimetableList.getId());
        courseModelTimetableList.setName(courseEntityTimetableList.getName());
        courseModelTimetableList.setTeacher(new TeacherModel(courseEntityTimetableList.getTeacher()
                                                                                      .getId()));
        return courseModelTimetableList;
    }
}
