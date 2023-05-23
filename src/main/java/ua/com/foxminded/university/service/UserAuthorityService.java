package ua.com.foxminded.university.service;

import ua.com.foxminded.university.dto.UserAuthorityDTO;
import ua.com.foxminded.university.exception.ServiceException;

public interface UserAuthorityService {
    
    public UserAuthorityDTO saveUserAuthority(UserAuthorityDTO user) 
            throws ServiceException;
}