package ua.com.foxminded.university.service;

import java.util.List;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
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
    public List<UserModel> getAllUsers() throws ServiceException {
        
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

