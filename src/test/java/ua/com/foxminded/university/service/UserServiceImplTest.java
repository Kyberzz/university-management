package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    
    public static final int FIRST_ELEMENT = 0;
    public static final String PASSWORD = "{noop}password";
    public static final String EMAIL = "email@com";
    
    @InjectMocks
    private UserServiceImpl userService;
    
    
    @Mock
    private UserRepository userRepositoryMock;
    
    @Test
    void getAllUsers_shouldExecuteCorrectNumberCalls() throws ServiceException {
        userService.getAll();
        verify(userRepositoryMock, times(1)).findAll();
    }

    @Test
    void getAllUsers_shouldReturnCorrectModels() throws ServiceException {
        UserEntity entity = new UserEntity();
        entity.setEmail(EMAIL);
        entity.setEnabled(true);
        entity.setPassword(PASSWORD);
        entity.setUserAuthority(new UserAuthorityEntity());
        entity.getUserAuthority().setRoleAuthority(RoleAuthority.ROLE_ADMIN);
        List<UserEntity> entities = new ArrayList<>();
        entities.add(entity);
        
        when(userRepositoryMock.findAll()).thenReturn(entities);
        
        List<UserModel> models = userService.getAll();
        UserModel model = models.get(FIRST_ELEMENT);
        
        assertEquals(EMAIL, model.getEmail());
        assertTrue(model.getEnabled());
        assertEquals(PASSWORD, model.getPassword());
        assertEquals(RoleAuthority.ROLE_ADMIN, model.getUserAuthority().getRoleAuthority());
        assertEquals(Authority.ADMIN, model.getUserAuthority().getAuthority());
    }
}
