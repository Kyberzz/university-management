package ua.com.foxminded.university.service;

public interface CourseService<T> {
    
    public int addCourseToTeacherById(int courseId, int teacherId);
    public int updateCourse(T course);
    public int removeCourseOfTeacherById(int courseId);
    public T getTimetableListByCourseId(int id);
}
