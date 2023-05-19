package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.UserAuthorityEntity;

public interface UserAuthorityRepository extends JpaRepository<UserAuthorityEntity, Integer> {
}