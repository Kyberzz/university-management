package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.CourseDTO;

public interface CourseService extends GenericService<CourseDTO> {
    
    public List<CourseDTO> getByTeacherId(int teacherId);
    
    public void deassignTeacherToCourse(int teacherId, int courseId);
    
    public void assignTeacherToCourse(int teacherId, int courseId);
    
    public CourseDTO getByIdWithLessonsAndTeachers(int courseId);
    
    public CourseDTO getByIdWithLessons(int courseId);
}
