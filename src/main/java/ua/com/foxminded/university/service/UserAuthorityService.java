package ua.com.foxminded.university.service;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserAuthorityModel;

public interface UserAuthorityService {
    
    public UserAuthorityModel saveUserAuthority(UserAuthorityModel user) 
            throws ServiceException;
}