package ua.com.foxminded.university.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.entity.Authority;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService<UserModel> {
    
    private UserRepository userRepository;
    private UserDetailsManager userDetailsManager;
    
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void deleteByEmail(String email) throws ServiceException {
        try {
            userDetailsManager.deleteUser(email);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting the user fails.", e);
        }
    }
    
    public void createUser(UserModel model) throws ServiceException {
        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<UserModel>> violation = validator.validate(model);
            
            if (!violation.isEmpty()) {
                throw new ConstraintViolationException(violation);
            } else {
                ModelMapper modelMapper = new ModelMapper();
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                
                modelMapper.typeMap(UserModel.class, UserEntity.class).addMappings(mapper -> {
                    mapper.map(src -> src.getUserAuthority().getAuthority(), 
                            (destination, value) -> destination.getUserAuthority()
                            .setAuthority((Authority)value));
                });
                
                UserEntity entity = modelMapper.map(model, UserEntity.class);
                
                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                UserDetails user = User.builder().username(entity.getEmail())
                        .password(entity.getPassword())
                        .passwordEncoder(encoder::encode)
                        .authorities(String.valueOf(entity.getUserAuthority().getAuthority()))
                        .disabled(!entity.getEnabled())
                        .build();
                userDetailsManager.createUser(user);
            }
        } catch (ValidationException | IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Creating user object fails.", e);
        }
    }
    
    @Override
    public List<UserModel> getNotAuthorizedUsers() throws ServiceException {
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            List<UserEntity> users = userRepository.findAll();
            List<UserEntity> notAuthorizedUsers = users.stream()
                    .filter(user -> {
                        if (user.getUserAuthority() != null && 
                            user.getUserAuthority().getAuthority() == null) {
                            return true;
                        } else {
                            return user.getUserAuthority() == null;
                        }
                    })
                    .collect(Collectors.toList());
            Type type = new TypeToken<List<UserModel>>() {}.getType();
            return modelMapper.map(notAuthorizedUsers, type);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting not authrized users fails.", e);
        }
    }
    
    @Override
    public UserModel getByEmail(String email) throws ServiceException {
        UserEntity entity = userRepository.findByEmail(email);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(entity, UserModel.class);
    }
    
    @Override
    public List<UserModel> getAllUsers() throws ServiceException {
        try {
            List<UserEntity> entities = userRepository.findAll();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            Type type = new TypeToken<List<UserModel>>() {}.getType();
            List<UserModel> models = modelMapper.map(entities, type);
            List<UserModel> namedModels;
            namedModels = models.stream()
                                .map(model -> {
                                    if (model.getTeacher() != null) {
                                        model.setFirstName(model.getTeacher().getFirstName());
                                        model.setLastName(model.getTeacher().getLastName());
                                    } else if (model.getStudent() != null) {
                                        model.setFirstName(model.getStudent().getFirstName());
                                        model.setLastName(model.getStudent().getLastName());
                                    } else if (model.getStaff() != null) {
                                        model.setFirstName(model.getStaff().getFirstName());
                                        model.setLastName(model.getStaff().getLastName());
                                    }
                                    return model;
                                })
                                .collect(Collectors.toList());
            return namedModels;
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all students fails", e);
        }
    }
    
    @Override
    public void updateUser(UserModel model) throws ServiceException {
        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validatro = factory.getValidator();
            Set<ConstraintViolation<UserModel>> violations = validatro.validate(model);
            
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            } else {
                ModelMapper modelMapper = new ModelMapper();
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                
                modelMapper.typeMap(UserModel.class, UserEntity.class).addMappings(mapper -> {
                    mapper.map(src -> src.getUserAuthority().getAuthority(), 
                               (destination, value) -> destination.getUserAuthority()
                                                                  .setAuthority((Authority)value));
                });
                
                UserEntity entity = modelMapper.map(model, UserEntity.class);
                
                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                UserDetails user = User.builder().username(entity.getEmail())
                                                 .password(entity.getPassword())
                                                 .passwordEncoder(encoder::encode)
                                                 .roles(String.valueOf(entity.getUserAuthority()
                                                                             .getAuthority()))
                                                 .disabled(!entity.getEnabled())
                                                 .build();
                
                userDetailsManager.updateUser(user);
            }
        } catch (ValidationException | IllegalArgumentException | ConfigurationException | 
                 MappingException  e) {
            throw new ServiceException("Updating user object fails.", e);
        }
    }
    
    @Override
    public UserModel getActiveUserByEmail(String email) throws ServiceException {
        try {
            UserEntity userEntity = userRepository.findActiveUserByEmail(email);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(userEntity, UserModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting user with its authority by its email failed", e);
        }
    }
}

