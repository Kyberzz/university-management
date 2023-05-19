package ua.com.foxminded.university.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.LessonEntity;

public interface LessonRepository extends JpaRepository<LessonEntity, Integer> {
    
    @Query("select l from LessonEntity l left join fetch l.group " + 
           "where l.datestamp = ?1 and l.lessonOrder =?2 and l.group.id = ?3")
    public LessonEntity findByDatestampAndGroupIdAndTimingId(LocalDate date,
                                                             int lessonOrder,                                                 
                                                             int groupId);
    
    public List<LessonEntity> findByDatestamp(LocalDate date);
    
    public LessonEntity findCourseById(Integer id);

    public LessonEntity findGroupById(Integer id);

    public LessonEntity findById(int id);
}
