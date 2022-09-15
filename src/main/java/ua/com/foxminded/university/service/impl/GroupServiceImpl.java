package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.model.WeekDayModel;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.ServiceException;

@Slf4j
@Service
public class GroupServiceImpl implements GroupService<GroupModel> {
    
    private GroupDao groupDao;
    
    @Autowired
    public GroupServiceImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }
    
    @Override
    public GroupModel getStudentListByGroupId(int id) throws ServiceException {
        GroupEntity groupEntityStudentsList = null;
        try {
            groupEntityStudentsList = groupDao.getStudentListByGroupId(id);
        } catch (DaoException e) {
            throw new ServiceException("Getting students list of the group failed.", e);
        }
       
        List<StudentModel> studentList = groupEntityStudentsList.getStudentList().stream()
                .map(entity -> {
                    StudentModel model = new StudentModel(entity.getId());
                    model.setFirstName(entity.getFirstName());
                    model.setGroup(new GroupModel(entity.getGroup().getId()));
                    model.setLastName(entity.getLastName());
                    return model;
                })
                .collect(Collectors.toList());
        GroupModel groupModelStudentsList = new GroupModel(groupEntityStudentsList.getId());
        groupModelStudentsList.setName(groupEntityStudentsList.getName());
        groupModelStudentsList.setStudentList(studentList);
        return groupModelStudentsList;
    }
    
    @Override
    public GroupModel getTimetableListByGroupId(int id) throws ServiceException {
        GroupEntity groupEntityTimetablesList = null;
        
        try {
            groupEntityTimetablesList = groupDao.getTimetableListByGroupId(id);
        } catch (DaoException e) {
            throw new ServiceException("Getting timebales list of the group failed.", e);
        }
        
        List<TimetableModel> timetableList = groupEntityTimetablesList.getTimetableList().stream()
                .map(entity -> {
                    TimetableModel model = new TimetableModel(entity.getId());
                    model.setCourse(new CourseModel(entity.getCourse().getId()));
                    model.setDescription(entity.getDescription());
                    model.setEndTime(entity.getEndTime());
                    model.setGroup(new GroupModel(entity.getGroup().getId()));
                    model.setStartTime(entity.getStartTime());
                    model.setWeekDay(WeekDayModel.valueOf(entity.getWeekDay().toString()));
                    return model;
                })
                .collect(Collectors.toList());
        GroupModel groupModelTimetablesList = new GroupModel(groupEntityTimetablesList.getId());
        groupModelTimetablesList.setName(groupEntityTimetablesList.getName());
        groupModelTimetablesList.setTimetableList(timetableList);
        return groupModelTimetablesList;
    }
}
