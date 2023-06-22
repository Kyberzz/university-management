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
import ua.com.foxminded.university.entity.UserAuthority;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.UserMother;

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
    
    private User persistedUser;

    @BeforeEach
    void init() {
        new TransactionTemplate(transactionManager).execute(transactionStatus -> {
            User user = UserMother.complete().build();
            persistedUser = userRepository.saveAndFlush(user);
            
            UserAuthority userAuthority = UserAuthority.builder()
                    .user(persistedUser)
                    .roleAuthority(RoleAuthority.ROLE_ADMIN)
                    .build();
            userAuthorityRepository.save(userAuthority);
            
            User userHasNoAuthority = User.builder()
                    .email(NOT_AUTHORIZED_EMAIL).build();
            userRepository.save(userHasNoAuthority);
            return null;
        });
    }
    
    @Test
    void findById_ShouldReturnEntityType() {
        User receivedUser = userRepository.findById(persistedUser.getId().intValue());
        assertEquals(persistedUser.getId(), receivedUser.getId());
    }

    @Test
    void findByUserAuthoritiesIsNull_ShouldReturnUsersThatHaveNoAuthorityObject() {
        List<User> users = userRepository.findByUserAuthorityIsNull();
        assertEquals(USERS_QUANTITY, users.size());
    }

    @Test
    void findByEmail_shouldReturnUser() {
        User receivedUser = userRepository.findByEmail(persistedUser.getEmail());
        assertEquals(persistedUser.getEmail(), receivedUser.getEmail());
    }
     
    @Test
    void findByPasswordIsNotNull_shouldReturnAllUsersHavingPassword() {
        List<User> receivedUsers = userRepository.findByPasswordIsNotNull();
        assertEquals(USERS_QUANTITY, receivedUsers.size());
    }
}
