package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.GroupEntity;

public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {

    public GroupEntity findTimetableListById(Integer id);

    public GroupEntity findStudentListById(Integer id);

    public GroupEntity findById(int id);
}
