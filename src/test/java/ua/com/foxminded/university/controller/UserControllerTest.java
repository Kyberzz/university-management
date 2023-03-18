package ua.com.foxminded.university.controller;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.model.UserAuthorityModel;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.modelmother.UserModelMother;
import ua.com.foxminded.university.service.UserService;

@ExtendWith(SpringExtension.class)
class UserControllerTest {
    
    public static final int ID = 1;
    public static final String NON_CONFIRM_PASSWORD = "pasF";
    
    @MockBean
    private UserService<UserModel> userServiceMock;
    
    private MockMvc mockMvc;
    private UserModel user;
    private UserAuthorityModel userAuthorit;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userServiceMock))
                                 .build();
        user = UserModelMother.complete().id(ID).build();
        userAuthorit = UserAuthorityModel.builder()
                .authority(Authority.ADMIN)
                .build();
    }
    
    @Test
    void authorizeUser_ShouldRednderNoConfirmView() throws Exception {
        mockMvc.perform(post("/users/authorize").param("email", user.getEmail())
                                                .param("password", user.getPassword())
                                                .param("passwordConfirm", NON_CONFIRM_PASSWORD)
                                                .flashAttr("userModel", user))
               .andExpect(view().name("users/no-confirm"));
    }
    
    @Test
    void authorizeUser_ShouldRenderNotFoundView() throws Exception {
        when(userServiceMock.getByEmail(anyString()))
            .thenThrow(new ServiceException());
        mockMvc.perform(post("/users/authorize").param("email", user.getEmail())
                                                .param("password", user.getPassword())
                                                .param("passwordConfirm", user.getPassword())
                                                .flashAttr("userModel", user))
               .andExpect(view().name("users/not-found"));
    }
    
    @Test
    void authorizeUser_ShouldAuthorizeUserAndRedirectToListView() throws Exception {
        when(userServiceMock.getByEmail(anyString())).thenReturn(user);
        
        mockMvc.perform(post("/users/authorize").param("email", user.getEmail())
                                                .param("password", user.getPassword())
                                                .param("passwordConfirm", user.getPassword())
                                                .flashAttr("userModel", user))
               .andExpect(redirectedUrl("/users/list"));
        verify(userServiceMock, times(1)).updateUser(isA(UserModel.class));
    }
    
    @Test
    void listAllUsers_ShouldAddAttributesAndRenderToListView() throws Exception {
        mockMvc.perform(get("/users/list"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("notAuthorizedUsers"))
               .andExpect(model().attributeExists("allUsers"))
               .andExpect(view().name("/users/list"));
        
        verify(userServiceMock, times(1)).getAll();
        verify(userServiceMock, times(1)).getNotAuthorizedUsers();
    }
    
    @Test
    void edit_ShouldEditUserAndRediredtToListView() throws Exception {
        user.setUserAuthority(userAuthorit);
        when(userServiceMock.getById(anyInt())).thenReturn(user);
        mockMvc.perform(post("/users/edit").param("userId", user.getId().toString())
                                           .flashAttr("userModel", user))
               .andExpect(redirectedUrl("users/list"));
        
        InOrder inOrder = Mockito.inOrder(userServiceMock);
        inOrder.verify(userServiceMock, times(1)).getById(anyInt());
        inOrder.verify(userServiceMock, times(1)).updateUser(isA(UserModel.class));
    }
    
    @Test
    void delete_ShouldDeleteUserAndRedirectToListView() throws Exception {
        
        mockMvc.perform(post("/users/delete").param("email", user.getEmail()))
               .andExpect(redirectedUrl("/users/list"));
        
        verify(userServiceMock, times(1)).deleteByEmail(user.getEmail());
    }
}
