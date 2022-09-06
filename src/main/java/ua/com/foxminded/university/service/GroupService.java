package ua.com.foxminded.university.service;

public interface GroupService<T> {
    
    public T getStudentListByGroupId(int id);
    public T getTimetableListByGroupId(int id);
}
