package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.UserDTO;

public interface UserService {
    
    public void deleteById(Integer id); 
    
    public void create(UserDTO model);
    
    public void update(UserDTO model);
    
    public UserDTO getById(int id);
    
    public List<UserDTO> getAll();

    public UserDTO getByEmail(String user);

    public void deleteByEmail(String email);

    public List<UserDTO> getNotAuthorizedUsers();
}