package ua.com.foxminded.university.service;

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

