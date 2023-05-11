package ua.com.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;

import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.UserEntityMother;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"prod", "testcontainers"})
@Transactional
class StudentControllerIntegrationTest {
    
    public static final String AUTHORIZED_EMAIL = "email@com";
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    
    @Autowired
    private WebApplicationContext context;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @BeforeTransaction
    void init() {
        new TransactionTemplate(transactionManager).execute(transactionStatus -> {
            userRepository.deleteAll();
            UserEntity user = UserEntityMother.complete().build();
            userRepository.saveAndFlush(user);
            UserAuthorityEntity userAuthority = UserAuthorityEntity.builder()
                    .roleAuthority(RoleAuthority.ROLE_ADMIN)
                    .user(user).build();
            userAuthorityRepository.save(userAuthority);
            return null;
        });
    }
    
    @AfterTransaction
    void cleanUp() {
        userRepository.deleteAll();
    }
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                 .apply(springSecurity()).build();
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void list_ShouldAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/students/list"))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().isOk());
    }
    
}