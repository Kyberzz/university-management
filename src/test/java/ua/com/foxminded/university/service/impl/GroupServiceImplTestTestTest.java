package ua.com.foxminded.university.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.service.ServiceException;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTestTestTest {
    
    private static final int GROUP_ID = 1;
    
    @InjectMocks
    private GroupServiceImpl groupService;
    
    @Mock
    private GroupRepository groupRepositoryMock;
    
    @Test
    void findStudentListById() throws RepositoryException, ServiceException {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setStudentList(new ArrayList<>());
        when(groupRepositoryMock.findStudentListById(ArgumentMatchers.anyInt())).thenReturn(groupEntity);
        groupService.getStudentListByGroupId(GROUP_ID);
        verify(groupRepositoryMock, times(1)).findStudentListById(ArgumentMatchers.anyInt());
    }
    @Test
    void findTimetableListById_CallingInnerMethods_CorrectCallQuantity() throws RepositoryException, 
                                                                                    ServiceException {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setTimetableList(new ArrayList<>());;
        when(groupRepositoryMock.findTimetableListById(ArgumentMatchers.anyInt())).thenReturn(groupEntity);
        groupService.getTimetableListByGroupId(GROUP_ID);
        verify(groupRepositoryMock, times(1)).findTimetableListById(ArgumentMatchers.anyInt());
    }
}
