package ua.com.foxminded.university.service.impl;

import static ua.com.foxminded.university.exception.ServiceErrorCode.*;

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
import org.springframework.dao.DataAccessException;
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
    public GroupDTO getGroupRelationsById(int id) {
        try {
            Group entity = groupRepository.getGroupRelationsById(id);
            return modelMapper.map(entity, GroupDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(GROUP_FETCH_ERROR, e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            groupRepository.deleteById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new ServiceException(GROUP_DELETE_ERROR, e);
        }
    }

    @Override
    public GroupDTO create(GroupDTO model) {
        try {
            Group entity = modelMapper.map(model, Group.class);
            Group createdEntity = groupRepository.saveAndFlush(entity);
            return modelMapper.map(createdEntity, GroupDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(GROUP_CREATE_ERROR, e);
        }
    }

    @Override
    public void update(GroupDTO model) {
        try {
            Group entity = modelMapper.map(model, Group.class);
            Group persistedEntity = groupRepository.findById(entity.getId().intValue());
            persistedEntity.setName(entity.getName());
            groupRepository.saveAndFlush(persistedEntity);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(GROUP_UPDATE_ERROR, e);
        }
    }

    @Override
    public GroupDTO getById(int id) {
        try {
            Group entity = groupRepository.findById(id);
            return modelMapper.map(entity, GroupDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                ConfigurationException | MappingException e) {
            throw new ServiceException(GROUP_FETCH_ERROR, e);
        }
    }
    
    @Override
    public List<GroupDTO> getAll() {
        try {
            List<Group> groupEntities = groupRepository.findAll();
            return modelMapper.map(groupEntities, GROUP_MODEL_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(GROUPS_FETCH_ERROR, e);
        }
    }
    
    @Override
    public GroupDTO getStudentsByGroupId(int id) {
        try {
            Group groupEntity = groupRepository.findStudentsById(id);
            return modelMapper.map(groupEntity, GroupDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(GROUP_FETCH_ERROR, e);
        }
    }
    
    @Override
    public GroupDTO getTimetablesByGroupId(int id) {
        try {
            Group groupEntity = groupRepository.findTimetablesById(id);
            return modelMapper.map(groupEntity, GroupDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(GROUP_FETCH_ERROR, e);
        }
    }
}
