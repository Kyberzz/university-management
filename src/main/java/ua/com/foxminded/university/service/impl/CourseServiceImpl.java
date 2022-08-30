package ua.com.foxminded.university.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService<CourseModel> {
    
    private static final int BAD_STATUS = 0;
    
    private CourseDao courseDao;
    private TeacherDao teacherDao;
    
    @Autowired
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }
    
    @Override
    public int addCourseToTeacherById(int courseId, int teacherId) {
        TeacherEntity teacher = teacherDao.getById(teacherId);
        CourseEntity course = courseDao.getById(teacherId);
        
        if (teacher == null || course == null) {
            return BAD_STATUS;
        } else {
            if (course.getTeacher().getId() == courseId) {
                return BAD_STATUS;
            } else {
                course.setTeacher(teacher);
                return courseDao.update(course);
            }
        }
    }
    
    @Override
    public int updateCourse(CourseModel courseModel) {
        CourseEntity courseEntity = courseDao.getById(courseModel.getId());
        
        if (courseEntity == null) {
            return BAD_STATUS;
        } else {
            courseEntity.setDescription(courseModel.getDescription());
            courseEntity.setId(courseModel.getId());
            courseEntity.setName(courseModel.getName());
            courseEntity.setTeacher(new TeacherEntity(courseModel.getId()));
            return courseDao.update(courseEntity);
        }
    }
   
    @Override
    public int removeCourseOfTeacherById(int courseId, int teacherId) {
        CourseEntity courseEntity = courseDao.getById(courseId);
        
        if (courseEntity == null) {
            return BAD_STATUS;
        } else {
            courseEntity.setTeacher(new TeacherEntity());
            return courseDao.update(courseEntity);
        }
    }
    
    
    @Override
    public CourseModel getTimetableListByCourseId(int id) {
        CourseEntity courseEntity = courseDao.getTimetableListByCourseId(id);
        CourseModel courseModel = new CourseModel();
        courseModel.setDescription(courseEntity.getDescription());
        courseModel.setId(courseEntity.getId());
        courseModel.setName(courseEntity.getName());
        courseModel.setTeacher(new TeacherModel(courseEntity.getTeacher().getId()));
        return courseModel;
    }
}
