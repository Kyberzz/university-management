package ua.com.foxminded.university.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    
    @Query("select l from Lesson l left join fetch l.group " + 
           "where l.datestamp = ?1 and l.lessonOrder =?2 and l.group.id = ?3")
    public Lesson findByDatestampAndGroupIdAndTimingId(LocalDate date,
                                                       int lessonOrder,                                                 
                                                       int groupId);
    
    public List<Lesson> findByDatestamp(LocalDate date);
    
    public Lesson findCourseById(Integer id);

    public Lesson findGroupById(Integer id);

    public Lesson findById(int id);
}
