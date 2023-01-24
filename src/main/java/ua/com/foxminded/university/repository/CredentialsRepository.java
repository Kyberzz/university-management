package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.entity.AuthorityEntity;
import ua.com.foxminded.university.exception.RepositoryException;

@Repository
public interface CredentialsRepository extends JpaRepository<AuthorityEntity, Integer> {
    
    public AuthorityEntity findByEmail(String email) throws RepositoryException;
}
