package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.model.CourseModel;
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
        TeacherEntity teacher = teacherDao.getCourseListByTeacherId(teacherId);
        Optional<CourseEntity> courseContainer;
        
        if (teacher == null) {
            return BAD_STATUS;
        } else {
            List<CourseEntity> teacherCourseList = teacher.getCourseList();
            courseContainer = teacherCourseList.stream()
                    .filter(courseEntity -> courseEntity.getId() == courseId)
                    .findFirst();
            
            if (courseContainer.isPresent()) {
                return BAD_STATUS;
            } else {
        //        CourseEntity courseEntity = courseDao.getById(courseId);
                
                if (courseEntity == null) {
                    return BAD_STATUS;
                } else {
                    courseEntity.getTeacher().setId(teacherId);;
                    return courseDao.update(courseEntity);
                }
            }
        }
    }
    
    @Override
    public int updateCourseOfTeacher(CourseModel courseModel) {
        TeacherEntity teacher = teacherDao.getById(courseModel.getTeacher().getId());
        Optional<CourseEntity> courseContainer;
        
        if (teacher == null) {
            return BAD_STATUS;
        } else {
            List<CourseEntity> allTeacherCourses = teacher.getCourseList();
            courseContainer = allTeacherCourses.stream()
                    .filter(entity -> entity.getId() == courseModel.getId())
                    .findFirst();
            
            if (!courseContainer.isPresent()) {
                return BAD_STATUS;
            } else {
                CourseEntity courseEntity = toCourseEntity(courseModel);
                return courseDao.update(courseEntity);
            }
        }
    }
   
    @Override
    public int removeCourseOfTeacherById(int courseId, int teacherId) {
        TeacherEntity teacher = teacherDao.getCourseListByTeacherId(teacherId);
        Optional<CourseEntity> courseContainer;

        if (teacher == null) {
            return BAD_STATUS;
        } else {
            List<CourseEntity> allTeacherCourses = teacher.getCourseList();
            courseContainer = allTeacherCourses.stream().filter(course -> course.getId()==courseId)
                                                     .findFirst();
           
            if (!courseContainer.isPresent()) {
                return BAD_STATUS;
            } else {
                CourseEntity course = courseContainer.get();
                course.setTeacher(null);
                return courseDao.update(course);
            }
        }
    }
    
    public CourseModel getTimetableListByCourseId(int id) {
        
        
        
    }
    
    private CourseEntity toCourseEntity(CourseModel model) {
        CourseEntity entity = new CourseEntity();
        entity.setDescription(model.getDescription());
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.getTeacher().setId(model.getTeacher().getId());
        return entity;
    }
}
