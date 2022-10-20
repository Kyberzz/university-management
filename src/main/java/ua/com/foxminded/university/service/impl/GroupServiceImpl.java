package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.model.WeekDayModel;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.ServiceException;

@Slf4j
@Service
public class GroupServiceImpl implements GroupService<GroupModel> {
    
    private GroupRepository groupDao;
    
    @Autowired
    public GroupServiceImpl(GroupRepository groupDao) {
        this.groupDao = groupDao;
    }
    
    @Override
    public GroupModel getStudentListByGroupId(int id) throws ServiceException {
        GroupEntity groupEntity = null;
        try {
            groupEntity = groupDao.getStudentListByGroupId(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Getting students list of the group failed.", e);
        }
       
        List<StudentModel> studentList = groupEntity.getStudentList().stream()
                .map(studentEntity -> {
                    StudentModel studentModel = new StudentModel();
                    studentModel.setId(studentEntity.getId());
                    studentModel.setFirstName(studentEntity.getFirstName());
                    GroupModel groupModel = new GroupModel();
                    groupModel.setId(studentEntity.getGroup().getId());
                    studentModel.setGroup(groupModel);
                    studentModel.setLastName(studentEntity.getLastName());
                    return studentModel;
                })
                .collect(Collectors.toList());
        
        GroupModel groupModel = new GroupModel();
        groupModel.setId(groupEntity.getId());
        groupModel.setName(groupEntity.getName());
        groupModel.setStudentList(studentList);
        return groupModel;
    }
    
    @Override
    public GroupModel getTimetableListByGroupId(int id) throws ServiceException {
        GroupEntity groupEntity = null;
        
        try {
            groupEntity = groupDao.getTimetableListByGroupId(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Getting timebales list of the group failed.", e);
        }
        
        List<TimetableModel> timetableList = groupEntity.getTimetableList().stream()
                .map(entity -> {
                    TimetableModel model = new TimetableModel();
                    model.setId(entity.getId());
                    CourseModel courseModel = new CourseModel();
                    courseModel.setId(entity.getCourse().getId());
                    model.setCourse(courseModel);
                    model.setDescription(entity.getDescription());
                    model.setEndTime(entity.getEndTime());
                    GroupModel groupModel = new GroupModel();
                    groupModel.setId(entity.getGroup().getId());
                    model.setGroup(groupModel);
                    model.setStartTime(entity.getStartTime());
                    model.setWeekDay(WeekDayModel.valueOf(entity.getWeekDay().toString()));
                    return model;
                })
                .collect(Collectors.toList());
        
        GroupModel groupModel = new GroupModel();
        groupModel.setId(groupEntity.getId());
        groupModel.setName(groupEntity.getName());
        groupModel.setTimetableList(timetableList);
        return groupModel;
    }
}
