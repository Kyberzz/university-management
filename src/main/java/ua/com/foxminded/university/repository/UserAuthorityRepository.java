package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.entity.UserAuthorityEntity;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthorityEntity, Integer> {
}
