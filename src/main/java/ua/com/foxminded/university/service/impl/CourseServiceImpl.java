package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
        CourseEntity courseEntity = null;
        
        try {
            courseEntity = courseDao.getTimetableListByCourseId(id);
        } catch (DaoException e) {
            throw new ServiceException("Getting timetable list of course id faled.", e);
        }
        
        List<TimetableModel> timetableList = courseEntity.getTimetableList()
                .stream()
                .map(entity -> {
                    TimetableModel model = new TimetableModel();
                    model.setId(entity.getId());
                    CourseModel course = new CourseModel();
                    course.setId(entity.getCourse().getId());
                    model.setCourse(course);
                    model.setDescription(entity.getDescription());
                    model.setEndTime(entity.getEndTime());
                    GroupModel group = new GroupModel();
                    group.setId(entity.getGroup().getId());
                    model.setGroup(group);
                    model.setStartTime(entity.getStartTime());
                    model.setWeekDay(WeekDayModel.valueOf(entity.getWeekDay().toString()));
                    return model;
                }).collect(Collectors.toList());
        
        CourseModel courseModel = new CourseModel();
        courseModel.setId(courseEntity.getId());
        courseModel.setTimetableList(timetableList);
        courseModel.setDescription(courseEntity.getDescription());
        courseModel.setName(courseEntity.getName());
        TeacherModel teacherModel = new TeacherModel();
        teacherModel.setId(courseEntity.getTeacher().getId());
        courseModel.setTeacher(teacherModel);
        return courseModel;
    }
}
