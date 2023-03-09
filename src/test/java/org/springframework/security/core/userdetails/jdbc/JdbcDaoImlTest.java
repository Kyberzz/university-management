package org.springframework.security.core.userdetails.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.UniversityManagement;
import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.UserEntityMother;

@SpringBootTest(classes = UniversityManagement.class)
@ActiveProfiles("test")
class JdbcDaoImlTest {
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private UserDetailsManager userDetailsManager;
    
    private UserEntity user;
    private UserAuthorityEntity userAuthority;
    
    @BeforeEach
    void init() {
        user = UserEntityMother.complete().build();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        
        userAuthority = UserAuthorityEntity.builder()
                                           .roleAuthority(RoleAuthority.ROLE_ADMIN)
                                           .user(user)
                                           .build();
        entityManager.persist(userAuthority);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetailsObjectByEmail() {
        UserDetails userDetails = userDetailsManager.loadUserByUsername(user.getEmail());
        assertEquals(user.getEnabled(), userDetails.isEnabled());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(userAuthority.getRoleAuthority().toString(),
                     userDetails.getAuthorities().iterator().next().getAuthority());
    }
}
