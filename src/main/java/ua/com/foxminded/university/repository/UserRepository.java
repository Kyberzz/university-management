package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    public List<User> findByPasswordIsNotNull();
    
    public List<User> findByUserAuthorityIsNull();
    
    public User findById(int id);
    
    public User findByEmail(String email);
}