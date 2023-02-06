package ua.com.foxminded.university.integreation;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Client;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import ua.com.foxminded.university.config.RepositoryTestConfig;
import ua.com.foxminded.university.controller.UserController;
import ua.com.foxminded.university.model.UserModel;

@AutoConfigureMockMvc
@SpringBootTest
//(classes = RepositoryTestConfig.class)
class UserControllerIntegrationTest {
    
    @Autowired
    private UserController userController;
    
    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void authorize_shouldAuthorizeExistingUser() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setId(2);
        userModel.setEmail("emmmm");
        
        mockMvc.perform(MockMvcRequestBuilders.post("/users/authorize")
                                              .flashAttr("userModel",userModel)
                                              .param("password", "4")
                                              .param("passwordConfirm", "4"))
               .andDo(print())
               .andExpect(MockMvcResultMatchers.status().isOk());
             //  .andExpect(MockMvcResultMatchers.forwardedUrl("error"));
             //  .andExpect(MockMvcResultMatchers.redirectedUrl("/users/list"));
    }
    
    @Test
    void listAllUsers_shouldRenderUserList() throws Exception {
        mockMvc.perform(get("/users/list")).andExpect(status().isOk())
                                           .andExpect(model().attributeExists("users"))
                                           .andExpect(model().attributeExists("userModel"));
    }
}
