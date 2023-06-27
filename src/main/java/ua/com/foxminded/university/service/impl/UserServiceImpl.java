package ua.com.foxminded.university.service.impl;

import static ua.com.foxminded.university.exception.ErrorCode.*;

import java.lang.reflect.Type;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.UserRepository;
import ua.com.foxminded.university.service.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    public static final Type USER_MODEL_LIST_TYPE = 
            new TypeToken<List<UserDTO>>() {}.getType();
    
    private final UserRepository userRepository;
    private final UserDetailsManager userDetailsManager;
    private final ModelMapper modelMapper;
    
    @Override
    public UserDTO updateUserPerson(UserDTO userDto) {
        try {
            User user = userRepository.findById(userDto.getId().intValue());
            user.getPerson().setFirstName(userDto.getPerson().getFirstName());
            user.getPerson().setLastName(userDto.getPerson().getLastName());
            User updatedUser = userRepository.saveAndFlush(user);
            return modelMapper.map(updatedUser, UserDTO.class);
        } catch (DataAccessException e) {
            throw new ServiceException(USER_UPDATE_ERROR, e);
        }
    }
    
    @Override
    public UserDTO updateEmail(int userId, String email) {
        try {
            User persistedUser = userRepository.findById(userId);
            persistedUser.setEmail(email);
            User updatedUser = userRepository.saveAndFlush(persistedUser);
            return modelMapper.map(updatedUser, UserDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(USER_EMAIL_DUPLICATION_ERROR, e);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(USER_UPDATE_ERROR, e);
        }
    }
    
    @Override
    public UserDTO createUserPerson(UserDTO dto) {
        try {
            User user = modelMapper.map(dto, User.class);
            User persistedUser = userRepository.saveAndFlush(User.builder()
                                                                 .person(user.getPerson())
                                                                 .build());
            return modelMapper.map(persistedUser, UserDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(PERSON_CREATE_ERROR, e);
        }
    }
    
    @Override
    public UserDTO getById(int id) {
        try {
            User entity = userRepository.findById(id);
            return modelMapper.map(entity, UserDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(USER_FETCH_ERROR, e);
        }
    }
    
    @Override
    public void deleteById(Integer id) {
        try {
            userRepository.deleteById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new ServiceException(USER_DELETE_ERROR, e);
        }
    }
    
    @Override
    public void deleteByEmail(String email) {
        try {
            userDetailsManager.deleteUser(email);
        } catch (IllegalArgumentException e) {
            throw new ServiceException(USER_DELETE_ERROR, e);
        }
    }
    
    @Override
    public void createNonPersonalizedUser(UserDTO dto) {
        try {
            PasswordEncoder encoder = PasswordEncoderFactories
                    .createDelegatingPasswordEncoder();
            String authority = String.valueOf(dto.getUserAuthority().getAuthority());
            UserDetails user = org.springframework.security.core.userdetails.User.builder()
                    .username(dto.getEmail())
                    .password(dto.getPassword())
                    .passwordEncoder(encoder::encode)
                    .authorities(authority)
                    .disabled(!dto.getEnabled())
                    .build();
            userDetailsManager.createUser(user);
        } catch (ConstraintViolationException e) {
            throw new ServiceException(USER_EMAIL_DUPLICATION_ERROR, e);
        } catch (RuntimeException e) {
            throw new ServiceException(USER_CREATE_ERROR, e);
        }
    }
    
    @Override
    public List<UserDTO> getNotAuthorizedUsers() {
        try {
            List<User> entities = userRepository.findByUserAuthorityIsNull();
            return modelMapper.map(entities, USER_MODEL_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(USER_FETCH_ERROR, e);
        }
    }
    
    @Override
    public UserDTO getByEmail(String email) {
        try {
            User entity = userRepository.findByEmail(email);
            return modelMapper.map(entity, UserDTO.class);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new ServiceException(USER_FETCH_ERROR, e);
        }
    }
    
    @Override
    public List<UserDTO> getAll() {
        try {
            List<User> entities = userRepository.findAll();
            return modelMapper.map(entities, USER_MODEL_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(USERS_FETCH_ERROR, e);
        }
    }
    
    @Override
    public void updateUser(UserDTO dto) throws ServiceException {
        try {
            PasswordEncoder encoder = PasswordEncoderFactories
                    .createDelegatingPasswordEncoder();
            UserDetails entity = org.springframework.security.core.userdetails.User.builder()
                    .username(dto.getEmail())
                    .password(dto.getPassword())
                    .passwordEncoder(encoder::encode)
                    .roles(String.valueOf(dto.getUserAuthority().getAuthority()))
                    .disabled(!dto.getEnabled()).build();
            userDetailsManager.updateUser(entity);
        } catch (ConstraintViolationException e) {
            throw new ServiceException(USER_EMAIL_DUPLICATION_ERROR, e);
        } catch (RuntimeException  e) {
            throw new ServiceException(USER_UPDATE_ERROR, e);
        }
    }
}
