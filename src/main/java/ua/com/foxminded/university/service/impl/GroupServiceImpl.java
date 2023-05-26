package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.service.GroupService;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    
    public static final Type GROUP_MODEL_LIST_TYPE = 
            new TypeToken<List<GroupDTO>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    
    @Override
    public void deassignGroup(int studentId) {
        Student student = studentRepository.findById(studentId);
        student.setGroup(null);
        studentRepository.saveAndFlush(student);
    }
    
    @Override
    public void sortContainedStudentsByLastName(GroupDTO group) {
        List<StudentDTO> list = new ArrayList<>(group.getStudents());
        Collections.sort(list, Comparator.comparing(student -> student.getUser()
                                                                      .getPerson()
                                                                      .getLastName()));
        Set<StudentDTO> set = new LinkedHashSet<>(list);
        group.setStudents(set);
    }
    
    @Override
    public void assignGroup(int groupId, int[] studentIds) {
        Arrays.stream(studentIds).forEach(studentId -> {
            Student student = studentRepository.findById(studentId);
            student.setGroup(new Group());
            student.getGroup().setId(groupId);
            studentRepository.saveAndFlush(student);
        });
    }
    
    @Override
    public GroupDTO getGroupRelationsById(int id) throws ServiceException {
        try {
            Group entity = groupRepository.getGroupRelationsById(id);
            return modelMapper.map(entity, GroupDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting group with its relations fails", e);
        }
    }

    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            groupRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting a group by its id fails", e);
        }
    }

    @Override
    public GroupDTO create(GroupDTO model) throws ServiceException {
        try {
            Group entity = modelMapper.map(model, Group.class);
            Group createdEntity = groupRepository.saveAndFlush(entity);
            return modelMapper.map(createdEntity, GroupDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating a group fails", e);
        }
    }

    @Override
    public void update(GroupDTO model) throws ServiceException {
        try {
            Group entity = modelMapper.map(model, Group.class);
            Group persistedEntity = groupRepository.findById(entity.getId().intValue());
            persistedEntity.setName(entity.getName());
            groupRepository.saveAndFlush(persistedEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Udating a group fails", e);
        }
    }

    @Override
    public GroupDTO getById(int id) throws ServiceException {
        try {
            Group entity = groupRepository.findById(id);
            return modelMapper.map(entity, GroupDTO.class);
        } catch (Exception e) {
            throw new ServiceException("Getting a group by its id fails", e);
        }
    }
    
    @Override
    public List<GroupDTO> getAll() throws ServiceException {
        try {
            List<Group> groupEntities = groupRepository.findAll();
            return modelMapper.map(groupEntities, GROUP_MODEL_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all groups was failed", e);
        }
    }
    
    @Override
    public GroupDTO getStudentsByGroupId(int id) throws ServiceException {
        try {
            Group groupEntity = groupRepository.findStudentsById(id);
            return modelMapper.map(groupEntity, GroupDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting students list of the group failed", e);
        }
    }
    
    @Override
    public GroupDTO getTimetablesByGroupId(int id) throws ServiceException {
        try {
            Group groupEntity = groupRepository.findTimetablesById(id);
            return modelMapper.map(groupEntity, GroupDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timebales list of the group failed", e);
        }
    }
}
