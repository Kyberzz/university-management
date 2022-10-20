package ua.com.foxminded.university.repository;


import ua.com.foxminded.university.entity.GroupEntity;

public interface GroupDao extends GenericDao<GroupEntity> {
    
    public GroupEntity getTimetableListByGroupId(int id) throws DaoException;
    public GroupEntity getStudentListByGroupId(int id) throws DaoException;
}
