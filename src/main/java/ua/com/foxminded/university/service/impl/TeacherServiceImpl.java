package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService<TeacherModel> {
    
    private final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);
    
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
            String errorMessage = "Getting the courses list by the teacher id failed.";
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
        
        TeacherModel teacherModelCourseList = new TeacherModel();
        
        List<CourseModel> courseEntityList = teacherEntityCoursesList.getCourseList().stream()
                .map(entity -> {
                    CourseModel model = new CourseModel();
                    model.setDescription(entity.getDescription());
                    model.setId(entity.getId());
                    model.setName(entity.getName());
                    model.setTeacher(new TeacherModel(entity.getTeacher().getId()));
                    return model;
                })
                .collect(Collectors.toList());
        
        teacherModelCourseList.setCourseList(courseEntityList);
        teacherModelCourseList.setFirstName(teacherEntityCoursesList.getFirstName());
        teacherModelCourseList.setId(teacherEntityCoursesList.getId());
        teacherModelCourseList.setLastName(teacherEntityCoursesList.getLastName());
        return teacherModelCourseList;
    }
}
