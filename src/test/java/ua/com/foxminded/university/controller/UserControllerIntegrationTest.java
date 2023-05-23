package ua.com.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import ua.com.foxminded.university.dto.UserAuthorityDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.entity.Authority;
import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthority;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.UserMother;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"prod", "testcontainers"})
@Transactional
class UserControllerIntegrationTest {

    public static final String ADMIN = "ADMIN";
    public static final String PASSWORD = "pass";
    public static final String EMAIL = "email@com";
    public static final String AUTHORIZED_EMAIL = "authorized@email";
    
    @Autowired
    private WebApplicationContext context;
    
    @Autowired
    private UserRepository userRepository;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    
    @Autowired
    private MockMvc mockMvc;
    private User authorizedUser;
    private User notAuthorizedUser;

    @BeforeTransaction
    void init() {
        User user = UserMother.complete().email(AUTHORIZED_EMAIL).build();
        authorizedUser = userRepository.saveAndFlush(user);
        UserAuthority userAuthority = UserAuthority.builder()
              .roleAuthority(RoleAuthority.ROLE_ADMIN)
              .user(authorizedUser)
              .build();
        userAuthorityRepository.saveAndFlush(userAuthority);
        
        notAuthorizedUser = UserMother.complete().build();
        userRepository.saveAndFlush(notAuthorizedUser);
    }
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                 .apply(springSecurity()).build();
    }
    
    @AfterTransaction
    void cleanUp() {
        userRepository.deleteAll();
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void edit_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        UserAuthorityDTO userAuthority = UserAuthorityDTO.builder()
                .authority(Authority.STAFF).build();
        UserDTO model = UserDTO.builder()
                                   .email(authorizedUser.getEmail())
                                   .enabled(false)
                                   .userAuthority(userAuthority).build();

        mockMvc.perform(post("/users/edit").with(csrf())
                                           .param("userId", authorizedUser.getId()
                                                                            .toString())
                                           .flashAttr("userModel", model))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void listAll_ShoulAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/users/list"))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void delete_ShouldDeleteUserByEmailAndRedirect() throws Exception {
        mockMvc.perform(post("/users/delete").param("email", notAuthorizedUser.getEmail())
                                             .with(csrf()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void authorize_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        UserAuthorityDTO userAuthorityModel = UserAuthorityDTO.builder()
                                                                  .authority(Authority.ADMIN)
                                                                  .build();
        UserDTO model = UserDTO.builder()
                                   .enabled(true)
                                   .userAuthority(userAuthorityModel)
                                   .build();

        mockMvc.perform(post("/users/authorize").param("email", notAuthorizedUser.getEmail())
                                                .param("password", PASSWORD)
                                                .param("passwordConfirm", PASSWORD)
                                                .flashAttr("userModel", model)
                                                .with(csrf()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
}
