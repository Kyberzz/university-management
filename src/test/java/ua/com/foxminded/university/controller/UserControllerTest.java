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

import ua.com.foxminded.university.dto.UserAuthorityDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.dtomother.UserDTOMother;
import ua.com.foxminded.university.entity.Authority;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.service.UserService;

@ExtendWith(SpringExtension.class)
class UserControllerTest {
    
    public static final int USER_ID = 1;
    public static final String BAD_CONTENT = "some string";
    public static final String NON_CONFIRM_PASSWORD = "pasF";
    
    @MockBean
    private UserService userServiceMock;
    
    private MockMvc mockMvc;
    private UserDTO userDto;
    private UserAuthorityDTO userAuthorityDto;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userServiceMock))
                                 .build();
        userAuthorityDto = UserAuthorityDTO.builder()
                                           .authority(Authority.ADMIN).build();
        userDto = UserDTOMother.complete().id(USER_ID)
                                          .userAuthority(userAuthorityDto).build();
    }
    
    @Test
    void authorize_ShouldReturnBadRequestStatus() throws Exception {
        
        mockMvc.perform(post("/users/authorize").param("email", userDto.getEmail())
                                                .param("password", userDto.getPassword())
                                                .param("passwordConfirm", userDto.getPassword())
                                                .content(BAD_CONTENT))
               .andExpect(status().is4xxClientError())
               .andExpect(view().name("error"));
    }
    
    @Test
    void authorize_ShouldRednderNoConfirmView() throws Exception {
        mockMvc.perform(post("/users/authorize").param("email", userDto.getEmail())
                                                .param("password", userDto.getPassword())
                                                .param("passwordConfirm", NON_CONFIRM_PASSWORD)
                                                .flashAttr("userModel", userDto))
               .andExpect(view().name("users/no-confirm"));
    }
    
    @Test
    void authorize_ShouldRenderNotFoundView() throws Exception {
        when(userServiceMock.getByEmail(anyString())).thenThrow(new ServiceException());
        mockMvc.perform(post("/users/authorize").param("email", userDto.getEmail())
                                                .param("password", userDto.getPassword())
                                                .param("passwordConfirm", userDto.getPassword())
                                                .flashAttr("userModel", userDto))
               .andExpect(view().name("users/not-found"));
    }
    
    @Test
    void authorize_ShouldAuthorizeUserAndRedirectToListView() throws Exception {
        when(userServiceMock.getByEmail(anyString())).thenReturn(userDto);
        
        mockMvc.perform(post("/users/authorize").param("email", userDto.getEmail())
                                                .param("password", userDto.getPassword())
                                                .param("passwordConfirm", userDto.getPassword())
                                                .flashAttr("userModel", userDto))
               .andExpect(redirectedUrl("/users/list"));
        verify(userServiceMock, times(1)).updateUser(isA(UserDTO.class));
    }
    
    @Test
    void listAll_ShouldAddAttributesAndRenderToListView() throws Exception {
        mockMvc.perform(get("/users/list"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("notAuthorizedUsers"))
               .andExpect(model().attributeExists("allUsers"))
               .andExpect(view().name("users/list"));
        
        verify(userServiceMock).getAll();
        verify(userServiceMock).getNotAuthorizedUsers();
    }
    
    @Test
    void update_ShouldReturnBadRequestStatus() throws Exception {
        userDto.setUserAuthority(userAuthorityDto);
        when(userServiceMock.getById(anyInt())).thenReturn(userDto);
        mockMvc.perform(post("/users/edit").param("userId", userDto.getId().toString())
                                           .content(BAD_CONTENT))
               .andExpect(status().is4xxClientError())
               .andExpect(view().name("error"));
    }
    
    @Test
    void edit_ShouldEditUserAndRediredtToListView() throws Exception {
        userDto.setUserAuthority(userAuthorityDto);
        when(userServiceMock.getById(anyInt())).thenReturn(userDto);
        mockMvc.perform(post("/users/edit").param("userId", userDto.getId().toString())
                                           .flashAttr("userModel", userDto))
               .andExpect(redirectedUrl("/users/list"));
        
        InOrder inOrder = Mockito.inOrder(userServiceMock);
        inOrder.verify(userServiceMock).getById(anyInt());
        inOrder.verify(userServiceMock).updateUser(isA(UserDTO.class));
    }
    
    @Test
    void delete_ShouldDeleteUserAndRedirectToListView() throws Exception {
        mockMvc.perform(post("/users/delete").param("email", userDto.getEmail()))
               .andExpect(redirectedUrl("/users/list"));
        
        verify(userServiceMock).deleteByEmail(userDto.getEmail());
    }
}
