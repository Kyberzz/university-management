package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.exception.ServiceException;

public interface UserService {
    
    public void deleteById(Integer id) throws ServiceException; 
    
    public void create(UserDTO model) throws ServiceException;
    
    public void update(UserDTO model) throws ServiceException;
    
    public UserDTO getById(int id) throws ServiceException;
    
    public List<UserDTO> getAll() throws ServiceException;

    public UserDTO getByEmail(String user) throws ServiceException;

    public void deleteByEmail(String email) throws ServiceException;

    public List<UserDTO> getNotAuthorizedUsers() throws ServiceException;
}