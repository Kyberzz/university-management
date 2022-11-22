package ua.com.foxminded.university.service.impl;

import javax.transaction.Transactional;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.ServiceException;


@Slf4j
@Transactional
@Service
public class GroupServiceImpl implements GroupService<GroupModel> {
    
    private GroupRepository groupRepository;
    
    @Autowired
    public GroupServiceImpl(GroupRepository groupDao) {
        this.groupRepository = groupDao;
    }
    
    @Override
    public GroupModel getStudentListByGroupId(int id) throws ServiceException {
        try {
            GroupEntity groupEntity = groupRepository.findStudentListById(id);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(groupEntity, GroupModel.class);
        } catch (RepositoryException | IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting students list of the group failed.", e);
        }
    }
    
    @Override
    public GroupModel getTimetableListByGroupId(int id) throws ServiceException {
        try {
            GroupEntity groupEntity = groupRepository.findTimetableListById(id);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(groupEntity, GroupModel.class);
        } catch (RepositoryException | IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timebales list of the group failed.", e);
        }
    }
}
