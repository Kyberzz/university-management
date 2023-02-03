package ua.com.foxminded.university.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService<UserModel> {
    
    UserRepository userRepository;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserModel> getAllUsers() {
        List<UserEntity> entities = userRepository.getAllHavingPassword();
        ModelMapper modelMapper = new ModelMapper();
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
    }
    
    @Override
    public void udateUser(UserModel model) throws ServiceException {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity entity = modelMapper.map(model, UserEntity.class);
        userRepository.saveAndFlush(entity);
    }
    
    @Override
    public UserModel getActiveUserAuthorityByEmail(String email) throws ServiceException {
        try {
            UserEntity userEntity = userRepository.findActiveUserByEmail(email);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(userEntity, UserModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting user with its authority by its email failed", e);
        }
    }
}

