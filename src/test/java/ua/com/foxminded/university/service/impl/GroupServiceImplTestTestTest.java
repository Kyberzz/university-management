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

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.service.ServiceException;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTestTestTest {
    
    private static final int GROUP_ID = 1;
    
    @InjectMocks
    private GroupServiceImpl groupService;
    
    @Mock
    private GroupDao groupDaoMock;
    
    @Test
    void getStudentListByGroupId() throws DaoException, ServiceException {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setStudentList(new ArrayList<>());
        when(groupDaoMock.getStudentListByGroupId(ArgumentMatchers.anyInt())).thenReturn(groupEntity);
        groupService.getStudentListByGroupId(GROUP_ID);
        verify(groupDaoMock, times(1)).getStudentListByGroupId(ArgumentMatchers.anyInt());
    }
    @Test
    void getTimetableListByGroupId_CallingInnerMethods_CorrectCallQuantity() throws DaoException, 
                                                                                    ServiceException {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setTimetableList(new ArrayList<>());;
        when(groupDaoMock.getTimetableListByGroupId(ArgumentMatchers.anyInt())).thenReturn(groupEntity);
        groupService.getTimetableListByGroupId(GROUP_ID);
        verify(groupDaoMock, times(1)).getTimetableListByGroupId(ArgumentMatchers.anyInt());
    }
}
