package ua.com.foxminded.university.service;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.CredentialsEntity;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CredentialsModel;
import ua.com.foxminded.university.repository.CredentialsRepository;

@Slf4j
@Service
public class CredentialsServiceImpl implements CredentialsService<CredentialsModel> {
    
    private CredentialsRepository credentialsRepository;
    
    @Autowired
    public CredentialsServiceImpl(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }
    
    @Override
    public List<CredentialsModel> getAll() throws ServiceException {
        try {
            List<CredentialsEntity> credentialsEntities = credentialsRepository.findAll();
            ModelMapper modelMapper = new ModelMapper();
            Type listType = new TypeToken<List<CredentialsModel>>() {}.getType();
            return modelMapper.map(credentialsEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all credentials failed", e);
        }
    }
    
    @Override
    public CredentialsModel getByEmail(String email) throws ServiceException {
        ModelMapper modelMapper = new ModelMapper();
        
        try {
            CredentialsEntity credentialsEntity = credentialsRepository.findByEmail(email);
            return modelMapper.map(credentialsEntity, CredentialsModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException | 
                 RepositoryException e) {
            throw new ServiceException("Getting credential by the email failed.", e);
        }
    }
}
