package ua.com.foxminded.university.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.LessonOrder;
import ua.com.foxminded.university.entity.ScheduleEntity;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {
    
//    @Query("select s from ScheduleEntity s left join fetch s.group " + 
//           "where s.datestamp = ?1 and s.lessonOrder = ?2 and s.group.id = ?3")
//    public ScheduleEntity findByDatestampAndLessonOrderAndGroupId(LocalDate date, 
//                                                                  LessonOrder order, 
//                                                                  int groupId);
    
    public List<ScheduleEntity> findByDatestamp(LocalDate date);
    
    public ScheduleEntity findCourseById(Integer id);

    public ScheduleEntity findGroupById(Integer id);

    public ScheduleEntity findById(int id);
}
