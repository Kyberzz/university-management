package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.com.foxminded.university.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    
    @Query("select u from UserEntity u join fetch u.authority "
         + "where u.email = :email and u.isActive = true")
    public UserEntity findActiveUserByEmail(@Param("email")String email);
}
