package ua.com.foxminded.university.service;

public interface CourseService<T> {
    
    public int addCourseToTeacherById(int courseId, int teacherId);
    public int updateCourseOfTeacher(T course);
    public int removeCourseOfTeacherById(int courseId, int teacherId);
    public T getTimetableListByCourseId(int id);
}
