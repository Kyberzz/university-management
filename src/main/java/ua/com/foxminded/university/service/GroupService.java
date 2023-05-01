package ua.com.foxminded.university.service;


import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.GroupModel;

public interface GroupService extends GenericService<GroupModel> {
    
    public GroupModel getGroupRelationsById(int id) throws ServiceException;

    public GroupModel getStudentsByGroupId(int id) throws ServiceException;

    public GroupModel getTimetablesByGroupId(int id) throws ServiceException;
}