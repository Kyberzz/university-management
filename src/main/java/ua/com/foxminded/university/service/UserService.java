package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.UserDTO;

public interface UserService {
    
    public UserDTO updateUserPerson(UserDTO user);
    
    public UserDTO updateEmail(int userId, String email);
    
    public UserDTO createUserPerson(UserDTO dto);
    
    public void deleteById(Integer id); 
    
    public void createNonPersonalizedUser(UserDTO dto);
    
    public void updateUser(UserDTO dto);
    
    public UserDTO getById(int id);
    
    public List<UserDTO> getAll();

    public UserDTO getByEmail(String user);

    public void deleteByEmail(String email);

    public List<UserDTO> getNotAuthorizedUsers();
}