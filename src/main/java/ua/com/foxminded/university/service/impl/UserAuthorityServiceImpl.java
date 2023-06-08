package ua.com.foxminded.university.service.impl;

import static ua.com.foxminded.university.exception.ServiceErrorCode.*;

import javax.transaction.Transactional;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
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
    public UserAuthorityDTO saveUserAuthority(UserAuthorityDTO model) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            UserAuthority entity = modelMapper.map(model, UserAuthority.class);
            UserAuthority persistedEntity = userAuthorityRepository.saveAndFlush(entity);
            return modelMapper.map(persistedEntity, UserAuthorityDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(USER_AUTHORITY_CREATE_ERROR, e);
        } 
    }
}