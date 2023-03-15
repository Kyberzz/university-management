package ua.com.foxminded.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.UserEntityMother;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.model.UserAuthorityModel;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles({"testcontainers", "prod"})
class UserControllerIntegrationTest {
    
    public static final String LAST_NAME = "Musk";
    public static final String FIRST_NAME = "Elon";
    public static final String EMAIL_NAME = "testy@com";
    public static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "newpassword";
    
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    
//    @Autowired
//    private UserDetailsManager userDetailsManager;
//    
    @Autowired
    private MockMvc mockMvc;
    
    private UserEntity user;
    private UserAuthorityEntity userAuthority;
    private UserModel userModel;
    
    @BeforeEach
    void setup() {
        user = UserEntityMother.complete().build();
        user = userRepository.saveAndFlush(user);
        userAuthority = UserAuthorityEntity.builder()
                .roleAuthority(RoleAuthority.ROLE_ADMIN)
                .user(user)
                .build();
        userAuthorityRepository.saveAndFlush(userAuthority);
        
//        userModel = new UserModel();
//        userModel.setEnabled(false);
//        userModel.setPassword(NEW_PASSWORD);
//        userModel.setUserAuthority(new UserAuthorityModel());
//        userModel.getUserAuthority().setAuthority(Authority.STAFF);
    }
    
    @AfterEach
    void cleanUp() {
        Optional<UserEntity> persistedUser = userRepository.findById(user.getId());
        
        if (persistedUser != null) {
            userRepository.delete(user);
        }
    }
    
    @Test
    void delete_ShouldDeleteUserAndRenderListView() throws Exception {
        mockMvc.perform(post("/users/delete").param("email", user.getEmail()))
               .andExpect(redirectedUrl("/users/list"));
    }
    
    /*
    @Test
    void edit_shouldEditUserDetails() throws Exception {
        String modelId = user.getId().toString(); 
        
        mockMvc.perform(MockMvcRequestBuilders.post("/users/edit")
                                              .flashAttr("userModel", userModel)
                                              .param("userId", modelId))
               .andDo(print())
               .andExpect(redirectedUrl("/users/list"));
    }
    */
    
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
