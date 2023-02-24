package ua.com.foxminded.university.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Client;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.config.RepositoryTestConfig;
import ua.com.foxminded.university.controller.UserController;
import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.model.UserAuthorityModel;
import ua.com.foxminded.university.model.UserModel;

//@TestPropertySource(locations = {"/application.properties"})
//@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerIntegrationTest {
    
    public static final String USERS_EDIT_URL = "/users/edit";
    public static final String USERS_LIST_URL = "/users/list";
    public static final String EMAIL_NAME = "gmail@com";
    public static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "newpassword";
    
//    @Container
//    public static PostgreSQLContainer<?> container = new PostgreSQLContainer("postgres:latest")
//            .withDatabaseName("university");
    
    
//    @PersistenceUnit
//    private EntityManagerFactory entityManagerFactory; 
    
    @Autowired
    private UserController userController;
    
    @Autowired
    private UserDetailsManager userDetailsManager;
    
    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        
        
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.builder().username(EMAIL_NAME)
                                         .password(PASSWORD)
                                         .passwordEncoder(encoder::encode)
                                         .authorities(Authority.ADMIN.toString())
                                         .disabled(false)
                                         .build();
        userDetailsManager.createUser(user);
        
    }
    
    
    @Test
    void edit_shouldPerformEditingUserDetails() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setEnabled(false);
        userModel.setPassword(NEW_PASSWORD);
        userModel.setUserAuthority(new UserAuthorityModel());
        userModel.getUserAuthority().setAuthority(Authority.STAFF);
        
        mockMvc.perform(MockMvcRequestBuilders.post(USERS_EDIT_URL)
                                              .flashAttr("userModel", userModel)
                                              .param("email", EMAIL_NAME))
               .andDo(print())
               .andExpect(redirectedUrl(USERS_LIST_URL));
    }
    
/*
    @Test
    void authorize_shouldAuthorizeExistingUser() throws Exception {
            UserModel userModel = new UserModel();
            userModel.setEmail("@@@@@@@@");
            
            mockMvc.perform(MockMvcRequestBuilders.post("/users/authorize")
                                                  .flashAttr("userModel",userModel)
                                                  .param("password", "4")
                                                  .param("passwordConfirm", "4"))
                   .andDo(print())
                   .andExpect(redirectedUrl("/users/list"));
    }
    
    @Test
    void listAllUsers_shouldRenderUserList() throws Exception {
        mockMvc.perform(get("/users/list")).andExpect(status().isOk())
                                           .andExpect(model().attributeExists("users"))
                                           .andExpect(model().attributeExists("userModel"));
    }
    */
}
