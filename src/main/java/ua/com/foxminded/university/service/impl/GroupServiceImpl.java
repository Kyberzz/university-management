package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.List;


import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.service.GroupService;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    
    private static final Type LIST_TYPE = new TypeToken<List<GroupModel>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final GroupRepository groupRepository;
    
    @Override
    public List<GroupModel> getAllGroups() throws ServiceException {
        try {
            List<GroupEntity> groupEntities = groupRepository.findAll();
            return modelMapper.map(groupEntities, LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all groups was failed", e);
        }
    }
    
    @Override
    public GroupModel getStudentListByGroupId(int id) throws ServiceException {
        try {
            GroupEntity groupEntity = groupRepository.findStudentListById(id);
            return modelMapper.map(groupEntity, GroupModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting students list of the group failed.", e);
        }
    }
    
    @Override
    public GroupModel getTimetableListByGroupId(int id) throws ServiceException {
        try {
            GroupEntity groupEntity = groupRepository.findTimetableListById(id);
            return modelMapper.map(groupEntity, GroupModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timebales list of the group failed.", e);
        }
    }
}
