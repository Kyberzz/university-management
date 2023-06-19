package ua.com.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.controller.DefaultControllerTest.ADMIN_EMAIL;
import static ua.com.foxminded.university.controller.UserController.CONFIRMATION_PASSWORD_PARAMETER;
import static ua.com.foxminded.university.controller.UserController.EMAIL_PARAMETER;
import static ua.com.foxminded.university.controller.UserController.PASSWORD_PARAMETER;
import static ua.com.foxminded.university.controller.UserController.USERS_ATTRIBUTE;
import static ua.com.foxminded.university.controller.UserController.USER_ATTRIBUTE;
import static ua.com.foxminded.university.controller.UserController.USER_ID_PARAMETER;

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
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entity.UserAuthority;
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
    private UserAuthorityDTO userAuthorityDto;
    private UserDTO userDto;

    @BeforeTransaction
    void init() {
        User user = UserMother.complete().email(ADMIN_EMAIL).build();
        authorizedUser = userRepository.saveAndFlush(user);
        UserAuthority userAuthority = UserAuthority.builder()
              .roleAuthority(RoleAuthority.ROLE_ADMIN)
              .user(authorizedUser)
              .build();
        userAuthorityRepository.saveAndFlush(userAuthority);
        
        notAuthorizedUser = UserMother.complete().build();
        userRepository.saveAndFlush(notAuthorizedUser);
        
        userAuthorityDto = UserAuthorityDTO.builder().authority(Authority.ADMIN).build();
        userDto = UserDTO.builder().enabled(true)
                                   .userAuthority(userAuthorityDto)
                                   .build();
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
    @WithUserDetails(ADMIN_EMAIL)
    void delete_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/users/{userId}/delete", notAuthorizedUser.getId())
                    .with(csrf()))
            .andExpect(authenticated().withRoles(ADMIN.toString()))
            .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void createPerson_ShouldRedirect_WhenUserHasEmail() throws Exception {
        mockMvc.perform(post("/users/create-user-person")
                    .flashAttr(USER_ATTRIBUTE, userDto)
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void createPerson_ShouldRedirect_WhenUserHasNoEmail() throws Exception {
        userDto.setEmail(null);
        mockMvc.perform(post("/users/create-user-person")
                    .flashAttr(USERS_ATTRIBUTE, userDto)
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/users/edit").param(USER_ID_PARAMETER, 
                                                  String.valueOf(notAuthorizedUser.getId()))
                                           .flashAttr(USER_ATTRIBUTE, userDto)
                                           .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))      
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void authorize_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/users/authorize").param(EMAIL_PARAMETER, 
                                                       notAuthorizedUser.getEmail())
                                                .param(PASSWORD_PARAMETER, PASSWORD)
                                                .param(CONFIRMATION_PASSWORD_PARAMETER, PASSWORD)
                                                .flashAttr(USER_ATTRIBUTE, userDto)
                                                .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAll_ShoulAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/users/list"))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().isOk());
    }
}
