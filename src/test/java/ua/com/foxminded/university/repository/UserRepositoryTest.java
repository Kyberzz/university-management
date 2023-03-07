package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.objectmother.UserEntityMother;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    public static final String EMAIL = "some@email";
    public static final int USERS_QUANTITY = 1;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void init() {
        user = UserEntityMother.complete().build();
        userRepository.saveAndFlush(user);
        UserAuthorityEntity userAuthority = UserAuthorityEntity.builder()
                .roleAuthority(RoleAuthority.ROLE_ADMIN)
                .user(user).build();
        user.setUserAuthority(userAuthority);
        userRepository.saveAndFlush(user);

        UserEntity userHasNoAuthority = UserEntity.builder()
                                                  .email(EMAIL)
                                                  .build();
        userRepository.saveAndFlush(userHasNoAuthority);
    }

    @Test
    void findByUserAuthorityIsEmpty_shouldReturnUsersThatHaveNoAuthorityObject() {
        List<UserEntity> users = userRepository.findByUserAuthorityIsNull();
        assertEquals(USERS_QUANTITY, users.size());
    }

    @Test
    void findByEmail_shouldReturnUser() {
        UserEntity receivedUser = userRepository.findByEmail(user.getEmail());
        assertEquals(user.getEmail(), receivedUser.getEmail());
    }
     
    @Test
    void findByPasswordIsNotNull_shouldReturnAllUsersHavingPassword() {
        List<UserEntity> receivedUsers = userRepository.findByPasswordIsNotNull();
        assertEquals(USERS_QUANTITY, receivedUsers.size());
    }
}
