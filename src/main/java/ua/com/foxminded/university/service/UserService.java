package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserModel;

public interface UserService extends GenericService<UserModel> {

    public UserModel getByEmail(String user) throws ServiceException;

    public void deleteByEmail(String email) throws ServiceException;

    public List<UserModel> getNotAuthorizedUsers() throws ServiceException;
}
