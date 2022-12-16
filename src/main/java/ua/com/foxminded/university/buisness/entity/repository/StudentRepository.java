package ua.com.foxminded.university.buisness.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.buisness.entity.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
    
    public StudentEntity findGroupById(Integer id) throws RepositoryException;
    public StudentEntity findById(int id) throws RepositoryException;
}
