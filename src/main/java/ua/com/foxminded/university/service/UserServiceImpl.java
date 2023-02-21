package ua.com.foxminded.university.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
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

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService<UserModel> {
    
    public static final String PREFIX = "ROLE_";
    
    private final UserRepository userRepository;
    private final UserDetailsManager userDetailsManager;
    
    @Override
    public void deleteByEmail(String email) throws ServiceException {
        try {
            userDetailsManager.deleteUser(email);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting the user fails.", e);
        }
    }
    
    @Override
    public void createUser(UserModel model) throws ServiceException {
        try {
            validate(model);

            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String authority = String.valueOf(model.getUserAuthority().getAuthority());
            UserDetails user = User.builder().username(model.getEmail())
                                             .password(model.getPassword())
                                             .passwordEncoder(encoder::encode)
                                             .authorities(authority)
                                             .disabled(!model.getEnabled())
                                             .build();
            userDetailsManager.createUser(user);
        } catch (ConstraintViolationException e) {
            throw new ServiceException("Creating user object fails.", e);
        }
    }
    
    @Override
    public List<UserModel> getNotAuthorizedUsers() throws ServiceException {
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            List<UserEntity> entities = userRepository.findAll();
            List<UserEntity> notAuthorizedUsers = entities.stream().filter(user -> {
                if (user.getUserAuthority() != null && user.getUserAuthority()
                                                           .getRoleAuthority() == null) {
                    return true;
                } else {
                    return user.getUserAuthority() == null;
                }
            }).collect(Collectors.toList());
            Type type = new TypeToken<List<UserModel>>() {
            }.getType();
            List<UserModel> models = modelMapper.map(notAuthorizedUsers, type);
            return setNonEntityProperties(models);
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
            return setNonEntityProperties(models);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all students fails", e);
        }
    }
    
    @Override
    public void updateUser(UserModel model) throws ServiceException {
        try {   
            validate(model);

            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            UserDetails user = User.builder().username(model.getEmail())
                                             .password(model.getPassword())
                                             .passwordEncoder(encoder::encode)
                                             .roles(String.valueOf(model.getUserAuthority()
                                                                        .getAuthority()))
                                             .disabled(!model.getEnabled()).build();

            userDetailsManager.updateUser(user);
        } catch (ConstraintViolationException  e) {
            throw new ServiceException("Updating user object fails.", e);
        }
    }
    
    private void  validate(UserModel model) throws ConstraintViolationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserModel>> violations = validator.validate(model);
        
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        } 
    }
    
    private List<UserModel> setNonEntityProperties(List<UserModel> models) {
        int prefixIndex = PREFIX.length();
        
        return models.stream().map(model -> {
            if (model.hasUserAuthority()) {
                RoleAuthority roleAuthority = model.getUserAuthority().getRoleAuthority();
                String authority = String.valueOf(roleAuthority).substring(prefixIndex);
                model.getUserAuthority().setAuthority(Authority.valueOf(authority));
            }
            
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
        }).collect(Collectors.toList());
    }
}

