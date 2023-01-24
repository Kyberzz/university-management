package ua.com.foxminded.university.service;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.repository.UserRepository;

public class UserServiceImpl implements UserService<UserModel> {
    
    UserRepository userRepository;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel getUserAuthorityByEmail(String email) throws ServiceException {
        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(userEntity, UserModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting user with its authority by its email failed", e);
        }
    }
}

