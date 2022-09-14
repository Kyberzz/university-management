package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.TeacherService;

@Slf4j
@Service
public class TeacherServiceImpl implements TeacherService<TeacherModel> {
    
    private TeacherDao teacherDao; 
    
    @Autowired
    public TeacherServiceImpl(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }
    
    @Override
    public TeacherModel getCourseListByTeacherId(int id) throws ServiceException {
        TeacherEntity teacherEntityCoursesList = null;
        
        try {
            teacherEntityCoursesList = teacherDao.getCourseListByTeacherId(id);
        } catch (DaoException e) {
            throw new ServiceException("Getting the courses list by the teacher id failed.", e);
        }
        
        List<CourseModel> courseEntityList = teacherEntityCoursesList.getCourseList().stream()
                .map(entity -> {
                    CourseModel model = new CourseModel(entity.getId());
                    model.setDescription(entity.getDescription());
                    model.setName(entity.getName());
                    model.setTeacher(new TeacherModel(entity.getTeacher().getId()));
                    return model;
                })
                .collect(Collectors.toList());
        TeacherModel teacherModelCourseList = new TeacherModel(teacherEntityCoursesList.getId());
        teacherModelCourseList.setCourseList(courseEntityList);
        teacherModelCourseList.setFirstName(teacherEntityCoursesList.getFirstName());
        teacherModelCourseList.setLastName(teacherEntityCoursesList.getLastName());
        return teacherModelCourseList;
    }
}
