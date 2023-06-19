package ua.com.foxminded.university.service;


import ua.com.foxminded.university.dto.GroupDTO;

public interface GroupService extends GenericService<GroupDTO> {
    
    public void deassignGroup(int studentId);
    
    public void sortContainedStudentsByLastName(GroupDTO group);
    
    public void assignGroup(int groupId, int[] studentIds);

    public GroupDTO getGroupRelationsById(int id);

    public GroupDTO getStudentsByGroupId(int id);

    public GroupDTO getTimetablesByGroupId(int id);
}
