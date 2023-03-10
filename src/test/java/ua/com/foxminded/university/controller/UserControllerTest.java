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
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.modelmother.UserModelMother;
import ua.com.foxminded.university.service.UserService;

@ExtendWith(SpringExtension.class)
class UserControllerTest {
    
    public static final int ID = 1;
    public static final String NON_CONFIRM_PASSWORD = "pasF";
    public static final String PASSWORD = "pass";
    public static final String STRING_ID = "1";
    public static final String EMAIL = "some@email";
    public static final String USERS_LIST_URL = "/users/list";
    public static final String USERS_DELETE_URL = "/users/delete";
    
    @MockBean
    private UserService<UserModel> userServiceMock;
    
    private MockMvc mockMvc;
    private UserModel userModel;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userServiceMock)).build();
        userModel = UserModelMother.complete().id(ID).build();
    }
    
    @Test
    void authorizeUser_ShouldRednderNoConfirmView() throws Exception {
        mockMvc.perform(post("/users/authorize").param("email", EMAIL)
                                                .param("password", PASSWORD)
                                                .param("passwordConfirm", NON_CONFIRM_PASSWORD)
                                                .flashAttr("userModel", userModel))
               .andExpect(view().name("users/no-confirm"));
    }
    
    @Test
    void authorizeUser_ShouldRenderNotFoundView() throws Exception {
        when(userServiceMock.getByEmail(anyString()))
            .thenThrow(new ServiceException());
        mockMvc.perform(post("/users/authorize").param("email", EMAIL)
                                                .param("password", PASSWORD)
                                                .param("passwordConfirm", PASSWORD)
                                                .flashAttr("userModel", userModel))
               .andExpect(view().name("users/not-found"));
    }
    
    @Test
    void authorizeUser_ShouldAuthorizeUserAndRedirectToListView() throws Exception {
        when(userServiceMock.getByEmail(anyString())).thenReturn(userModel);
        
        mockMvc.perform(post("/users/authorize").param("email", EMAIL)
                                                .param("password", PASSWORD)
                                                .param("passwordConfirm", PASSWORD)
                                                .flashAttr("userModel", userModel))
               .andExpect(redirectedUrl("/users/list"));
        verify(userServiceMock, times(1)).updateUser(isA(UserModel.class));
    }
    
    @Test
    void listAllUsers_ShouldAddAttributesAndRenderToListView() throws Exception {
        mockMvc.perform(get("/users/list"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("notAuthorizedUsers"))
               .andExpect(model().attributeExists("allUsers"))
               .andExpect(view().name("users/list"));
        
        verify(userServiceMock, times(1)).getAll();
        verify(userServiceMock, times(1)).getNotAuthorizedUsers();
    }
    
    @Test
    void edit_ShouldEditUserAndRediredtToListView() throws Exception {
        when(userServiceMock.getById(anyInt())).thenReturn(userModel);
        mockMvc.perform(post("/users/edit").param("userId", STRING_ID)
                                           .flashAttr("userModel", userModel))
               .andExpect(redirectedUrl("/users/list"));
        
        InOrder inOrder = Mockito.inOrder(userServiceMock);
        inOrder.verify(userServiceMock, times(1)).getById(anyInt());
        inOrder.verify(userServiceMock, times(1)).updateUser(isA(UserModel.class));
    }
    
    @Test
    void delete_ShouldDeleteUserAndRedirectToListView() throws Exception {
        
        mockMvc.perform(post("/users/delete").param("email", EMAIL))
               .andExpect(redirectedUrl("/users/list"));
        
        verify(userServiceMock, times(1)).deleteByEmail(EMAIL);
    }
}
