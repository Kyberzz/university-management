package ua.com.foxminded.university.service;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserAuthorityModel;
import ua.com.foxminded.university.repository.UserAuthorityRepository;

@Service
public class UserAuthorityServiceImpl implements UserAuthorityService<UserAuthorityModel> {
    
    private UserAuthorityRepository userAuthorityRepository;
    
    @Autowired
    public UserAuthorityServiceImpl(UserAuthorityRepository userAuthorityRepository) {
        this.userAuthorityRepository = userAuthorityRepository;
    }

    public UserAuthorityModel saveUserAuthority(UserAuthorityModel model) throws ServiceException {
        try {
            ModelMapper modelMapper = new ModelMapper();
            UserAuthorityEntity entity = modelMapper.map(model, UserAuthorityEntity.class);
            UserAuthorityEntity persistedEntity = userAuthorityRepository.saveAndFlush(entity);
            return modelMapper.map(persistedEntity, UserAuthorityModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Persisting UserAuthority object fais.", e);
        }
    }
}
