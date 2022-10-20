package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.TeacherService;

@Slf4j
@Service
public class TeacherServiceImpl implements TeacherService<TeacherModel> {
    
    private TeacherRepository teacherDao; 
    
    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherDao) {
        this.teacherDao = teacherDao;
    }
    
    @Override
    public TeacherModel getCourseListByTeacherId(int id) throws ServiceException {
        TeacherEntity teacherEntity = null;
        
        try {
            teacherEntity = teacherDao.getCourseListByTeacherId(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Getting the courses list by the teacher id failed.", e);
        }
        
        List<CourseModel> courseModelList = teacherEntity.getCourseList().stream()
                .map(entity -> {
                    CourseModel model = new CourseModel();
                    model.setId(entity.getId());
                    model.setDescription(entity.getDescription());
                    model.setName(entity.getName());
                    TeacherModel teacherModel = new TeacherModel();
                    teacherModel.setId(entity.getTeacher().getId());
                    model.setTeacher(teacherModel);
                    return model;
                })
                .collect(Collectors.toList());
        
        TeacherModel teacherModel = new TeacherModel();
        teacherModel.setId(teacherEntity.getId());
        teacherModel.setCourseList(courseModelList);
        teacherModel.setFirstName(teacherEntity.getFirstName());
        teacherModel.setLastName(teacherEntity.getLastName());
        return teacherModel;
    }
}
