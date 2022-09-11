package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class GroupServiceImpl implements GroupService<GroupModel> {
    
    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
    
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
            String errorMessage = "Getting students list of the group failed.";
            logger.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
       
        GroupModel groupModelStudentsList = new GroupModel();
        List<StudentModel> studentList = groupEntityStudentsList.getStudentList().stream()
                .map(entity -> {
                    StudentModel model = new StudentModel();
                    model.setFirstName(entity.getFirstName());
                    model.setGroup(new GroupModel(entity.getGroup().getId()));
                    model.setId(entity.getId());
                    model.setLastName(entity.getLastName());
                    return model;
                })
                .collect(Collectors.toList());
        groupModelStudentsList.setId(groupEntityStudentsList.getId());
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
            String errorMessage = "Getting timebales list of the group failed.";
            logger.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
        
        GroupModel groupModelTimetablesList = new GroupModel();
        
        List<TimetableModel> timetableList = groupEntityTimetablesList.getTimetableList().stream()
                .map(entity -> {
                    TimetableModel model = new TimetableModel();
                    model.setCourse(new CourseModel(entity.getCourse().getId()));
                    model.setDescription(entity.getDescription());
                    model.setEndTime(entity.getEndTime());
                    model.setGroup(new GroupModel(entity.getGroup().getId()));
                    model.setId(entity.getId());
                    model.setStartTime(entity.getStartTime());
                    model.setWeekDay(WeekDayModel.valueOf(entity.getWeekDay().toString()));
                    return model;
                })
                .collect(Collectors.toList());
        groupModelTimetablesList.setId(groupEntityTimetablesList.getId());
        groupModelTimetablesList.setName(groupEntityTimetablesList.getName());
        groupModelTimetablesList.setTimetableList(timetableList);
        return groupModelTimetablesList;
    }
}
