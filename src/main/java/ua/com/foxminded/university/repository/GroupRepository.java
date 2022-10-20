package ua.com.foxminded.university.repository;


import ua.com.foxminded.university.entity.GroupEntity;

public interface GroupRepository extends GenericRepository<GroupEntity> {
    
    public GroupEntity getTimetableListByGroupId(int id) throws RepositoryException;
    public GroupEntity getStudentListByGroupId(int id) throws RepositoryException;
}
