package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
    /*
    @Query("select s from StudentEntity s left join fetch s.user u")
    public List<StudentEntity> getAllStudentsIncludingEmails() throws RepositoryException;
    */
    public StudentEntity findGroupById(Integer id);
    public StudentEntity findById(int id);
}
