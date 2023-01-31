package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.exception.RepositoryException;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
    
    @Query("select s from StudentEntity s left join fetch s.user u")
    public List<StudentEntity> getAllStudentsIncludingEmails() throws RepositoryException;
    public StudentEntity findGroupById(Integer id) throws RepositoryException;
    public StudentEntity findById(int id) throws RepositoryException;
}
