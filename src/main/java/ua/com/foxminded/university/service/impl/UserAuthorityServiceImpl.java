package ua.com.foxminded.university.service.impl;

import javax.transaction.Transactional;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserAuthorityModel;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.service.UserAuthorityService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthorityServiceImpl implements UserAuthorityService<UserAuthorityModel> {
    
    private final UserAuthorityRepository userAuthorityRepository;
    
    @Override
    public UserAuthorityModel saveUserAuthority(
            UserAuthorityModel model) throws ServiceException {
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
