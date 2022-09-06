package ua.com.foxminded.university.dao;


import ua.com.foxminded.university.entity.GroupEntity;

public interface GroupDao extends GenericDao<GroupEntity> {
    
    public GroupEntity getTimetableListByGroupId(int id);
    public GroupEntity getStudentListByGroupId(int id);
}
