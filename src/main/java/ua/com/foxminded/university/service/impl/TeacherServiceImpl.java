package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService<TeacherModel> {
    
    private TeacherDao teacherDao; 
    
    @Autowired
    public TeacherServiceImpl(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }
    
    @Override
    public TeacherModel getCourseListByTeacherId(int id) {
        TeacherEntity teacherEntityCourseList = teacherDao.getCourseListByTeacherId(id);
        TeacherModel teacherModelCourseList = new TeacherModel();
        
        List<CourseModel> courseEntityList = teacherEntityCourseList.getCourseList().stream()
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
        teacherModelCourseList.setFirstName(teacherEntityCourseList.getFirstName());
        teacherModelCourseList.setId(teacherEntityCourseList.getId());
        teacherModelCourseList.setLastName(teacherEntityCourseList.getLastName());
        return teacherModelCourseList;
    }
}
