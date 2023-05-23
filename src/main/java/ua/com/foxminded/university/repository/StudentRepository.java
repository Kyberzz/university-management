package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    public Student findGroupById(Integer id);

    public Student findById(int id);
}
