package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
    
    public UserDTO getById(int id) throws ServiceException {
        try {
            User entity = userRepository.findById(id);
            return modelMapper.map(entity, UserDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting user by its id fails.", e);
        }
    }
    
    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            userRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting the user fails", e);
        }
    }
    
    @Override
    public void deleteByEmail(String email) throws ServiceException {
        try {
            userDetailsManager.deleteUser(email);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting the user fails.", e);
        }
    }
    
    @Override
    public void create(UserDTO dto) throws ServiceException {
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
            throw new ServiceException("Creating the user fails.", e);
        }
    }
    
    @Override
    public List<UserDTO> getNotAuthorizedUsers() throws ServiceException {
        try {
            List<User> entities = userRepository.findByUserAuthorityIsNull();
            return modelMapper.map(entities, USER_MODEL_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting not authrized users fails.", e);
        }
    }
    
    @Override
    public UserDTO getByEmail(String email) throws ServiceException {
        try {
            User entity = userRepository.findByEmail(email);
            return modelMapper.map(entity, UserDTO.class);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Getting the email fails", e);
        }
    }
    
    @Override
    public List<UserDTO> getAll() throws ServiceException {
        try {
            List<User> entities = userRepository.findAll();
            return modelMapper.map(entities, USER_MODEL_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all students fails", e);
        }
    }
    
    @Override
    public void update(UserDTO dto) throws ServiceException {
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
        } catch (ConstraintViolationException  e) {
            throw new ServiceException("Updating user object fails.", e);
        }
    }
}
