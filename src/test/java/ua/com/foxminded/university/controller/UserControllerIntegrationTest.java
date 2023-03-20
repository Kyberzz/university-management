package ua.com.foxminded.university.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultHandlers.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.UserEntityMother;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.model.UserAuthorityModel;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.repository.UserRepository;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"prod", "testcontainers"})
class UserControllerIntegrationTest {

    public static final String PASSWORD = "pass";
    public static final String EMAIL = "email@com";
    public static final String AUTHORIZED_EMAIL = "authorized@email";
    
    @Autowired
    UserRepository userRepository;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private MockMvc mockMvc;

    private UserEntity user;
    private UserEntity notAuthorizedUser;

    @BeforeTransaction
    void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        user = UserEntityMother.complete().email(AUTHORIZED_EMAIL).build();
        entityManager.persist(user);
       
        UserAuthorityEntity userAuthority = UserAuthorityEntity.builder()
              .roleAuthority(RoleAuthority.ROLE_ADMIN)
              .user(user)
              .build();
        entityManager.persist(userAuthority);
        
        notAuthorizedUser = UserEntityMother.complete()
                                            .password(null)
                                            .enabled(null).build();
        entityManager.persist(notAuthorizedUser);
        entityManager.getTransaction().commit();
        entityManager.close();
        
//        UserEntity user = UserEntityMother.complete().email(AUTHORIZED_EMAIL).build();
//        persistedUser = userRepository.saveAndFlush(user);
//        UserAuthorityEntity userAuthority = UserAuthorityEntity.builder()
//              .roleAuthority(RoleAuthority.ROLE_ADMIN)
//              .user(persistedUser)
//              .build();
//        userAuthorityRepository.saveAndFlush(userAuthority);
//        
//        notAuthorizedUser = UserEntityMother.complete().build();
//        userRepository.saveAndFlush(notAuthorizedUser);
    }
    
//    @AfterTransaction
//    void cleanUp() {
//        userRepository.deleteAll();
//    }
    
//    @BeforeEach 
//    @Transactional
//    void init() {
//        user = userRepository.findById(user.getId().intValue());
//        log.error("----------------" + user.getUserAuthorities().get(0).getRoleAuthority());
//    }
    
//    @Test
//    @WithUserDetails(AUTHORIZED_EMAIL)
//    @Transactional
//    void listAllUsers_ShoulAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
//        mockMvc.perform(get("/users/list"))
//               .andExpect(authenticated().withRoles(ADMIN))
//               .andExpect(status().isOk());
//    }
    
    @Test
    @Transactional
    @WithUserDetails("a")
    void authorizeUser_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        UserAuthorityModel userAuthorityModel = UserAuthorityModel.builder()
                                                                  .authority(Authority.ADMIN)
                                                                  .build();
        UserModel userModel = UserModel.builder()
                                       .enabled(true)
//                                       .userAuthority(userAuthorityModel)
                                       .build();
        mockMvc.perform(post("/users/authorize").param("email", EMAIL)
                                                .param("password", PASSWORD)
                                                .param("passwordConfirm", PASSWORD)
                                                .flashAttr("userModel", userModel))
               .andDo(exportTestSecurityContext())
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(redirectedUrl("/users/list"));
    }
}
