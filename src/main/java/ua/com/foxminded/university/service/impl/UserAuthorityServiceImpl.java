package ua.com.foxminded.university.service.impl;

import javax.transaction.Transactional;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.UserAuthorityDTO;
import ua.com.foxminded.university.entity.UserAuthority;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.service.UserAuthorityService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthorityServiceImpl implements UserAuthorityService {
    
    private final UserAuthorityRepository userAuthorityRepository;
    
    @Override
    public UserAuthorityDTO saveUserAuthority(UserAuthorityDTO model) 
            throws ServiceException {
        try {
            ModelMapper modelMapper = new ModelMapper();
            UserAuthority entity = modelMapper.map(model, UserAuthority.class);
            UserAuthority persistedEntity = userAuthorityRepository.saveAndFlush(entity);
            return modelMapper.map(persistedEntity, UserAuthorityDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Persisting UserAuthority object fais.", e);
        }
    }
}