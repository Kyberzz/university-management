package ua.com.foxminded.university.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.isA;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import ua.com.foxminded.university.dto.PersonDTO;
import ua.com.foxminded.university.dto.UserAuthorityDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.entity.Authority;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.UserMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.modelmother.PersonDtoMother;
import ua.com.foxminded.university.modelmother.UserDtoMother;
import ua.com.foxminded.university.repository.UserRepository;
import ua.com.foxminded.university.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    
    public static final int USER_ID = 1;
    public static final int FIRST_ELEMENT = 0;
    public static final String LAST_NAME = "Einstein";
    public static final String FIRST_NAME = "Albert";
    public static final String PASSWORD = "{noop}password";
    public static final String EMAIL = "email@com";
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @Mock
    private UserRepository userRepositoryMock;
    
    @Mock
    private JdbcUserDetailsManager userDetailsManagerMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private User user;
    private UserDTO userDto;
    private List<User> users;
    
    @BeforeEach
    void init() {
        user = UserMother.complete().build();
        PersonDTO personModel = PersonDtoMother.complete().build();
        UserAuthorityDTO userAuthorityModel = UserAuthorityDTO.builder()
                .authority(Authority.ADMIN).build();
        userDto = UserDtoMother.complete()
                               .person(personModel)
                               .userAuthority(userAuthorityModel).build();
        users = new ArrayList<>();
        users.add(user);
    }
    
    @Test
    void getById_shouldExecuteCorrectNumberCalls() throws ServiceException {
        when(userRepositoryMock.findById(USER_ID)).thenReturn(user);
        userService.getById(USER_ID);
        verify(userRepositoryMock).findById(anyInt());
        verify(modelMapperMock).map(user, UserDTO.class);
    }
    
    @Test
    void deleteByEmail_shouldExecuteCorrectNumberCalls() throws ServiceException {
        userService.deleteByEmail(EMAIL);
        verify(userDetailsManagerMock).deleteUser(EMAIL);
    }

    @Test
    void createUser_shouldExecuteCorrectNumberCalls() throws ServiceException {
        userService.createNonPersonalizedUser(userDto);
        verify(userDetailsManagerMock).createUser(isA(UserDetails.class));
    }

    @Test
    void getNotAuthorizedUsers_shouldExecuteCorrectNumberCalls() throws ServiceException {
        when(userRepositoryMock.findByUserAuthorityIsNull()).thenReturn(users);
        Type type = new TypeToken<List<UserDTO>>() {}.getType();
        userService.getNotAuthorizedUsers();
        verify(userRepositoryMock).findByUserAuthorityIsNull();
        verify(modelMapperMock).map(users, type);
    }

    @Test
    void getByEmail_shouldExecuteCorrectNumberCalls() throws ServiceException {
        when(userRepositoryMock.findByEmail(isA(String.class))).thenReturn(user);
        
        userService.getByEmail(EMAIL);
        verify(userRepositoryMock).findByEmail(isA(String.class));
        verify(modelMapperMock).map(user, UserDTO.class);
    }

    @Test
    void getAll_shouldReturnCorrectModels() throws ServiceException {
        userService.getAll();
        verify(userRepositoryMock).findAll();
        verify(modelMapperMock).map(ArgumentMatchers.<User>anyList(), 
                                              isA(Type.class));
    }
    
    @Test
    void updateUser() throws ServiceException {
        userService.updateUser(userDto);
        verify(userDetailsManagerMock).updateUser(isA(UserDetails.class));
    }
}
