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
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.service.GroupService;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    
    public static final Type GROUP_MODEL_LIST_TYPE = 
            new TypeToken<List<GroupModel>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    
    @Override
    public void deassignGroup(int studentId) {
        StudentEntity student = studentRepository.findById(studentId);
        student.setGroup(null);
        studentRepository.saveAndFlush(student);
    }
    
    @Override
    public void sortStudentsByLastName(GroupModel group) {
        List<StudentModel> list = new ArrayList<>(group.getStudents());
        Collections.sort(list, Comparator.comparing(
                student -> student.getUser().getPerson().getLastName()));
        Set<StudentModel> set = new LinkedHashSet<>(list);
        group.setStudents(set);
    }
    
    @Override
    public void assignGroup(int groupId, int[] studentIds) {
        Arrays.stream(studentIds).forEach(studentId -> {
            StudentEntity student = studentRepository.findById(studentId);
            student.setGroup(new GroupEntity());
            student.getGroup().setId(groupId);
            studentRepository.saveAndFlush(student);
        });
    }
    
    @Override
    public GroupModel getGroupRelationsById(int id) throws ServiceException {
        try {
            GroupEntity entity = groupRepository.getGroupRelationsById(id);
            return modelMapper.map(entity, GroupModel.class);
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
    public void create(GroupModel model) throws ServiceException {
        try {
            GroupEntity entity = modelMapper.map(model, GroupEntity.class);
            groupRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating a group fails", e);
        }
    }

    @Override
    public void update(GroupModel model) throws ServiceException {
        try {
            GroupEntity entity = modelMapper.map(model, GroupEntity.class);
            GroupEntity persistedEntity = groupRepository.findById(entity.getId().intValue());
            persistedEntity.setName(entity.getName());
            groupRepository.saveAndFlush(persistedEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Udating a group fails", e);
        }
    }

    @Override
    public GroupModel getById(int id) throws ServiceException {
        try {
            GroupEntity entity = groupRepository.findById(id);
            return modelMapper.map(entity, GroupModel.class);
        } catch (Exception e) {
            throw new ServiceException("Getting a group by its id fails", e);
        }
    }
    
    @Override
    public List<GroupModel> getAll() throws ServiceException {
        try {
            List<GroupEntity> groupEntities = groupRepository.findAll();
            return modelMapper.map(groupEntities, GROUP_MODEL_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all groups was failed", e);
        }
    }
    
    @Override
    public GroupModel getStudentsByGroupId(int id) throws ServiceException {
        try {
            GroupEntity groupEntity = groupRepository.findStudentsById(id);
            return modelMapper.map(groupEntity, GroupModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting students list of the group failed", e);
        }
    }
    
    @Override
    public GroupModel getTimetablesByGroupId(int id) throws ServiceException {
        try {
            GroupEntity groupEntity = groupRepository.findTimetablesById(id);
            return modelMapper.map(groupEntity, GroupModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timebales list of the group failed", e);
        }
    }
}
