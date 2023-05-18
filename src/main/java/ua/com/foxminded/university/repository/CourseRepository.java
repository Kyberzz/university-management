package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {
    
    
    @Query("select c from CourseEntity c left join fetch c.teachers "
                                      + "left join fetch c.schedules "
                                      + "where c.id = ?1")
    public CourseEntity getCourseRelationsById(int id);
    
    public CourseEntity findTimetablesById(Integer id);

    public CourseEntity findById(int id);
}
