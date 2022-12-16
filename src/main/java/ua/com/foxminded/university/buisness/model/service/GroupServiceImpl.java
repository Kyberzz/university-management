package ua.com.foxminded.university.buisness.model.service;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.buisness.entity.GroupEntity;
import ua.com.foxminded.university.buisness.entity.repository.GroupRepository;
import ua.com.foxminded.university.buisness.entity.repository.RepositoryException;
import ua.com.foxminded.university.buisness.model.GroupModel;


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
