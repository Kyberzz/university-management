package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Query("select g from Group g left join fetch g.students " + 
           "left join fetch g.lessons where g.id = ?1")
    public Group getGroupRelationsById(int id);
    
    public Group findTimetablesById(Integer id);

    public Group findStudentsById(Integer id);

    public Group findById(int id);
}
