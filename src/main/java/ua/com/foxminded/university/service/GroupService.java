package ua.com.foxminded.university.service;


import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.exception.ServiceException;

public interface GroupService extends GenericService<GroupDTO> {
    
    public void deassignGroup(int studentId);
    
    public void sortStudentsByLastName(GroupDTO group);
    
    public void assignGroup(int groupId, int[] studentIds);

    public GroupDTO getGroupRelationsById(int id) throws ServiceException;

    public GroupDTO getStudentsByGroupId(int id) throws ServiceException;

    public GroupDTO getTimetablesByGroupId(int id) throws ServiceException;
}