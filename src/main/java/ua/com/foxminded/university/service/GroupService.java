package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.GroupModel;

public interface GroupService {

    public List<GroupModel> getAllGroups() throws ServiceException;

    public GroupModel getStudentListByGroupId(int id) throws ServiceException;

    public GroupModel getTimetableListByGroupId(int id) throws ServiceException;
}
