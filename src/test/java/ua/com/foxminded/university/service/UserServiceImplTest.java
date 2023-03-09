package ua.com.foxminded.university.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
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

import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.UserEntityMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.model.PersonModel;
import ua.com.foxminded.university.model.UserAuthorityModel;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.modelmother.PersonModelMother;
import ua.com.foxminded.university.modelmother.UserModelMother;
import ua.com.foxminded.university.repository.UserRepository;

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
    private ValidatorServiceImpl<UserModel> validatorServiceMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private UserEntity entity;
    private UserModel model;
    private List<UserEntity> entities;
    
    @BeforeEach
    void init() {
        entity = UserEntityMother.complete().build();
        PersonModel personModel = PersonModelMother.complete().build();
        UserAuthorityModel userAuthorityModel = UserAuthorityModel.builder()
                .authority(Authority.ADMIN)
                .build();
        model = UserModelMother.complete()
                               .person(personModel)
                               .userAuthority(userAuthorityModel)
                               .build();
        entities = new ArrayList<>();
        entities.add(entity);
    }
    
    @Test
    void getById_shouldExecuteCorrectNumberCalls() throws ServiceException {
        when(userRepositoryMock.findById(USER_ID)).thenReturn(entity);
        userService.getById(USER_ID);
        verify(userRepositoryMock, times(1)).findById(anyInt());
        verify(modelMapperMock, times(1)).map(entity, UserModel.class);
    }
    
    @Test
    void deleteByEmail_shouldExecuteCorrectNumberCalls() throws ServiceException {
        userService.deleteByEmail(EMAIL);
        verify(userDetailsManagerMock, times(1)).deleteUser(EMAIL);
    }

    @Test
    void createUser_shouldExecuteCorrectNumberCalls() throws ServiceException {
        userService.createUser(model);
        verify(validatorServiceMock, times(1)).validate(isA(UserModel.class));
        verify(userDetailsManagerMock, times(1)).createUser(isA(UserDetails.class));
    }

    @Test
    void getNotAuthorizedUsers_shouldExecuteCorrectNumberCalls() throws ServiceException {
        when(userRepositoryMock.findByUserAuthorityIsNull()).thenReturn(entities);
        Type type = new TypeToken<List<UserModel>>() {}.getType();
        userService.getNotAuthorizedUsers();
        verify(userRepositoryMock, times(1)).findByUserAuthorityIsNull();
        verify(modelMapperMock, times(1)).map(entities, type);
    }

    @Test
    void getByEmail_shouldExecuteCorrectNumberCalls() throws ServiceException {
        when(userRepositoryMock.findByEmail(isA(String.class))).thenReturn(entity);
        
        userService.getByEmail(EMAIL);
        verify(userRepositoryMock, times(1)).findByEmail(isA(String.class));
        verify(modelMapperMock, times(1)).map(entity, UserModel.class);
    }

    @Test
    void getAll_shouldReturnCorrectModels() throws ServiceException {
        userService.getAll();
        verify(userRepositoryMock, times(1)).findAll();
        verify(modelMapperMock, times(1)).map(ArgumentMatchers.<UserEntity>anyList(), 
                                              isA(Type.class));
    }
    
    @Test
    void updateUser() throws ServiceException {
        userService.updateUser(model);
        verify(validatorServiceMock, times(1)).validate(model);
        verify(userDetailsManagerMock, times(1)).updateUser(isA(UserDetails.class));
    }
}
