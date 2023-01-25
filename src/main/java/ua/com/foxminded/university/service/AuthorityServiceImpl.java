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

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.Authorities;
import ua.com.foxminded.university.entity.AuthorityEntity;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.AuthorityModel;
import ua.com.foxminded.university.repository.AuthorityRepository;

@Slf4j
@Service
public class AuthorityServiceImpl implements AuthorityService<AuthorityModel> {
    
    private AuthorityRepository authorityRepository;
    
    @Autowired
    public AuthorityServiceImpl(AuthorityRepository credentialsRepository) {
        this.authorityRepository = credentialsRepository;
    }
    
    @Override
    public AuthorityModel getAllAuthoritySorts() throws ServiceException {
    List<AuthorityEntity> authorities = authorityRepository.findAll();
    List<Authorities> authorityKinds = authorities.stream().map(AuthorityEntity::getAuthority)
                                                           .distinct()
                                                           .collect(Collectors.toList());
    AuthorityModel credentials = new AuthorityModel();
    credentials.setAuthorities(authorityKinds);
    return credentials;
    }
    
    @Override
    public List<AuthorityModel> getAll() throws ServiceException {
        try {
            List<AuthorityEntity> credentialsEntities = authorityRepository.findAll();
            ModelMapper modelMapper = new ModelMapper();
            Type listType = new TypeToken<List<AuthorityModel>>() {}.getType();
            return modelMapper.map(credentialsEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all credentials failed", e);
        }
    }
}
