package ua.com.foxminded.university.controller;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.university.controller.UserController.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
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
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
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
    void delete_ShouldRedirectToGetAll() throws Exception {
        mockMvc.perform(post("/users/{userId}/delete", USER_ID))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(USERS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        verify(userServiceMock).deleteById(anyInt());
    }
    
    @Test
    void createPerson_ShouldRedirectToUsersListTemplate_WhenUserHasEmail() throws Exception {
        when(userServiceMock.createUserPerson(isA(UserDTO.class))).thenReturn(userDto);
        
        mockMvc.perform(post("/users/create-user-person")
                    .flashAttr(USER_ATTRIBUTE, userDto))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(USERS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        
        verify(userServiceMock).updateEmail(anyInt(), anyString());
        verify(userServiceMock).createUserPerson(isA(UserDTO.class));
    }
   
    @Test
    void createPerson_ShouldRedirectToUsersListTemplate_WhenUserHasNoEmail() throws Exception {
        userDto.setEmail(null);
        
        mockMvc.perform(post("/users/create-user-person")
                    .flashAttr(USERS_ATTRIBUTE, userDto))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(USERS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        
        verify(userServiceMock).createUserPerson(isA(UserDTO.class));
    }
    
    @Test
    void deleteAuthority_ShouldRedirectToUsersListTemplate() throws Exception {
        mockMvc.perform(post("/users/delete").param(EMAIL_PARAMETER, userDto.getEmail()))
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(USERS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        
        verify(userServiceMock).deleteByEmail(userDto.getEmail());
    }
    
    @Test
    void update_ShouldReturnBadRequestStatus() throws Exception {
        userDto.setUserAuthority(userAuthorityDto);
        
        mockMvc.perform(post("/users/edit").param(USER_ID_PARAMETER, String.valueOf(USER_ID))
                                           .content(BAD_CONTENT))
               .andExpect(status().is4xxClientError())
               .andExpect(view().name(ERROR_TEMPLATE_NAME));
    }
    
    @Test
    void update_ShouldEditUserAndRediredtToListView() throws Exception {
        userDto.setUserAuthority(userAuthorityDto);
        when(userServiceMock.getById(anyInt())).thenReturn(userDto);
        
        mockMvc.perform(post("/users/edit").param(USER_ID_PARAMETER, String.valueOf(USER_ID))
                                           .flashAttr(USER_ATTRIBUTE, userDto))
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(USERS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        
        InOrder inOrder = Mockito.inOrder(userServiceMock);
        inOrder.verify(userServiceMock).getById(anyInt());
        inOrder.verify(userServiceMock).updateUser(isA(UserDTO.class));
    }
    
    @Test
    void authorize_ShouldRednderNoConfirmTemplate() throws Exception {
        mockMvc.perform(post("/users/authorize").param(EMAIL_PARAMETER, userDto.getEmail())
                                                .param(PASSWORD_PARAMETER, userDto.getPassword())
                                                .param(CONFIRMATION_PASSWORD_PARAMETER, 
                                                       NON_CONFIRM_PASSWORD)
                                                .flashAttr(USER_ATTRIBUTE, userDto))
               .andExpect(view().name(NO_CONFRIM_TEMPLATE_PATH));
    }
    
    @Test
    void authorize_ShouldRenderNotFoundTemplate() throws Exception {
        when(userServiceMock.getByEmail(anyString())).thenThrow(new ServiceException());
        mockMvc.perform(post("/users/authorize").param(EMAIL_PARAMETER, userDto.getEmail())
                                                .param(PASSWORD_PARAMETER, userDto.getPassword())
                                                .param(CONFIRMATION_PASSWORD_PARAMETER, 
                                                       userDto.getPassword())
                                                .flashAttr(USER_ATTRIBUTE, userDto))
               .andExpect(view().name(NOT_FOUND_USER_TEMPLATE_PATH));
    }
    
    @Test
    void authorize_ShouldRedirectToGetAll() throws Exception {
        mockMvc.perform(post("/users/authorize").param(EMAIL_PARAMETER, userDto.getEmail())
                                                .param(PASSWORD_PARAMETER, userDto.getPassword())
                                                .param(CONFIRMATION_PASSWORD_PARAMETER, 
                                                       userDto.getPassword())
                                                .flashAttr(USER_ATTRIBUTE, userDto))
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(USERS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        
        verify(userServiceMock).getByEmail(anyString());
        verify(userServiceMock).updateUser(isA(UserDTO.class));
    }
    
    @Test
    void getAll_ShouldRenderUsesrsListTemplate() throws Exception {
        mockMvc.perform(get("/users/list"))
               .andExpect(model().attributeExists(NOT_AUTHORIZED_USERS_ATTRIBUTE, 
                                                  USERS_ATTRIBUTE))
               .andExpect(view().name(USERS_LIST_TEMPLATE_PATH));
        
        verify(userServiceMock).getAll();
        verify(userServiceMock).getNotAuthorizedUsers();
    }
}
