package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer>, 
                                           StudentCustomRepository<StudentEntity> {
    
    public StudentEntity findGroupById(Integer id) throws RepositoryException;
}
