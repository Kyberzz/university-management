package ua.com.foxminded.university.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.UserEntityMother;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"prod", "testcontainers"})
@Transactional(propagation = Propagation.REQUIRES_NEW)
class UserControllerIntegrationTest {

    public static final String EMAIL = "email@com";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private MockMvc mockMvc;

    private UserEntity persistedUser;

    @BeforeTransaction
    void init() {
        UserEntity user = UserEntityMother.complete().build();
        persistedUser = userRepository.saveAndFlush(user);
        UserAuthorityEntity userAuthority = UserAuthorityEntity.builder()
              .roleAuthority(RoleAuthority.ROLE_ADMIN)
              .user(persistedUser)
              .build();
        userAuthorityRepository.saveAndFlush(userAuthority);
        
        
//        persistedUser = new TransactionTemplate(transactionManager)
//                .execute(transactionStatus -> {
//            UserEntity user = UserEntityMother.complete().build();
//            return userRepository.save(user);
//        });
//        
//        new TransactionTemplate(transactionManager).execute(transactionStatus -> {
//            UserAuthorityEntity userAuthority = UserAuthorityEntity.builder()
//                    .roleAuthority(RoleAuthority.ROLE_ADMIN)
//                    .build();
//            userAuthority.setUser(persistedUser);
//            userAuthorityRepository.save(userAuthority);
//            return null;
//        });
    }
    
    @Test
    @WithUserDetails(EMAIL)
    void listAllUsersShouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/users/list"))
               .andExpect(status().isOk())
               .andExpect(view().name("users/list"));
    }
}
