package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.GroupEntity;

public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {

    @Query("select g from GroupEntity g left join fetch g.students "
                                     + "left join fetch g.timetables "
                                     + "where g.id = ?1")
    public GroupEntity getGroupRelationsById(int id);
    
    public GroupEntity findTimetablesById(Integer id);

    public GroupEntity findStudentsById(Integer id);

    public GroupEntity findById(int id);
}
