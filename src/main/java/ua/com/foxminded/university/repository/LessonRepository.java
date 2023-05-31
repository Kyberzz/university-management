package ua.com.foxminded.university.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    
    public Lesson findByTeacherIdAndLessonOrderAndCourseId(int teacherId, 
                                                           int lessonOrder, 
                                                           int courseId);
    
    public Lesson findByDatestampAndLessonOrderAndGroupsId(LocalDate date,
                                                           int lessonOrder,                                                 
                                                           int groupId);
    
    public List<Lesson> findByDatestamp(LocalDate date);
    
    public Lesson findCourseById(Integer id);

    public Lesson findGroupById(Integer id);

    public Lesson findById(int id);
}
