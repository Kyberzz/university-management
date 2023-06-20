package ua.com.foxminded.university.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    
    public List<Lesson> findByGroupsId(int groupId);
    
    public Lesson findByDatestampAndGroupsIdAndLessonOrder(
            LocalDate localDate, int groupId, int lessonorder);
    
    public List<Lesson> findByDatestampAndGroupsId(LocalDate localDate, int groupId);
    
    public List<Lesson> findByTeacherId(int teacherId);
    
    public List<Lesson> findByDatestampAndTeacherId(LocalDate localDate, int teacherId);
    
    public Lesson findByDatestampAndTeacherIdAndLessonOrder(
            LocalDate date, int teacherId, int lessonOrder);
    
    public Lesson findByDatestampAndTeacherIdAndLessonOrderAndCourseId(
            LocalDate date, int teacherId, int lessonOrder, int courseId);
    
    public Lesson findByDatestampAndLessonOrderAndGroupsId(
            LocalDate date, int lessonOrder, int groupId);
    
    public List<Lesson> findByDatestamp(LocalDate date);
    
    public Lesson findCourseById(Integer id);

    public Lesson findGroupById(Integer id);

    public Lesson findById(int id);
}
