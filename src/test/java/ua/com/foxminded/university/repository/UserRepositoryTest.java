package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.UserEntityMother;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRepositoryTest {

    public static final String NOT_AUTHORIZED_EMAIL = "some@email";
    public static final int USERS_QUANTITY = 1;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    private UserEntity persistedUser;

    @BeforeEach
    void init() {
        new TransactionTemplate(transactionManager).execute(transactionStatus -> {
            UserEntity user = UserEntityMother.complete().build();
            persistedUser = userRepository.saveAndFlush(user);
            
            UserAuthorityEntity userAuthority = UserAuthorityEntity.builder()
                    .user(persistedUser)
                    .roleAuthority(RoleAuthority.ROLE_ADMIN)
                    .build();
            userAuthorityRepository.save(userAuthority);
            
            UserEntity userHasNoAuthority = UserEntity.builder()
                    .email(NOT_AUTHORIZED_EMAIL).build();
            userRepository.save(userHasNoAuthority);
            return null;
        });
    }
    
    @Test
    void findById_ShouldReturnEntityType() {
        UserEntity receivedUser = userRepository.findById(persistedUser.getId().intValue());
        assertEquals(persistedUser.getId(), receivedUser.getId());
    }

    @Test
    void findByUserAuthoritiesIsNull_ShouldReturnUsersThatHaveNoAuthorityObject() {
        List<UserEntity> users = userRepository.findByUserAuthorityIsNull();
        assertEquals(USERS_QUANTITY, users.size());
    }

    @Test
    void findByEmail_shouldReturnUser() {
        UserEntity receivedUser = userRepository.findByEmail(persistedUser.getEmail());
        assertEquals(persistedUser.getEmail(), receivedUser.getEmail());
    }
     
    @Test
    void findByPasswordIsNotNull_shouldReturnAllUsersHavingPassword() {
        List<UserEntity> receivedUsers = userRepository.findByPasswordIsNotNull();
        assertEquals(USERS_QUANTITY, receivedUsers.size());
    }
}
