package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.univesity.exception.RepositoryException;

public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {
    
    public GroupEntity findTimetableListById(Integer id) throws RepositoryException;
    public GroupEntity findStudentListById(Integer id) throws RepositoryException;
    public GroupEntity findById(int id) throws RepositoryException;
}
