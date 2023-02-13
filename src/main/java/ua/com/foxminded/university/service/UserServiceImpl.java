package ua.com.foxminded.university.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.entity.Authority;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService<UserModel> {
    
    private UserRepository userRepository;
    
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void deleteById(Integer id) throws ServiceException {
        try {
            userRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting the user fails.", e);
        }
    }
    
    @Override
    public UserModel getUserById(int id) throws ServiceException {
        UserEntity entity = userRepository.findById(id);
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(entity, UserModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting user by the id fails.", e);
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
    public void updateUser(UserModel user) throws ServiceException {
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            
            modelMapper.typeMap(UserModel.class, UserEntity.class).addMappings(mapper -> {
                mapper.map(src -> src.getUserAuthority().getAuthority(), 
                           (destination, value) -> destination.getUserAuthority()
                                                              .setAuthority((Authority)value));
            });
            UserEntity entity = modelMapper.map(user, UserEntity.class);
            userRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating user failes", e);
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

