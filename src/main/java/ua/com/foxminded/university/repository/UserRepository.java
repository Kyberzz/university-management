package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    
    
    public List<UserEntity> findByPasswordIsNotNull();
    
    public List<UserEntity> findByUserAuthorityIsNull();
    
    public UserEntity findById(int id);
    
    public UserEntity findByEmail(String email);
}
