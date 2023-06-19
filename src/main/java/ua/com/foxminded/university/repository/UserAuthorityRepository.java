package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.UserAuthority;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Integer> {

}
