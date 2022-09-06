package ua.com.foxminded.university.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.model.WeekDayModel;
import ua.com.foxminded.university.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService<GroupModel> {
    
    private GroupDao groupDao;
    
    @Autowired
    public GroupServiceImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }
    
    @Override
    public GroupModel getStudentListByGroupId(int id) {
        GroupEntity groupEntityStudentList = groupDao.getStudentListByGroupId(id);
        GroupModel groupModelStudentList = new GroupModel();
        
        List<StudentModel> studentList = groupEntityStudentList.getStudentList().stream()
                .map(entity -> {
                    StudentModel model = new StudentModel();
                    model.setFirstName(entity.getFirstName());
                    model.setGroup(new GroupModel(entity.getGroup().getId()));
                    model.setId(entity.getId());
                    model.setLastName(entity.getLastName());
                    return model;
                })
                .collect(Collectors.toList());
        
        groupModelStudentList.setId(groupEntityStudentList.getId());
        groupModelStudentList.setName(groupEntityStudentList.getName());
        groupModelStudentList.setStudentList(studentList);
        return groupModelStudentList;
    }
    
    @Override
    public GroupModel getTimetableListByGroupId(int id) {
        GroupEntity groupEntityTimetableList = groupDao.getTimetableListByGroupId(id);
        GroupModel groupModelTimetableList = new GroupModel();
        
        List<TimetableModel> timetableList = groupEntityTimetableList.getTimetableList().stream()
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
        
        groupModelTimetableList.setId(groupEntityTimetableList.getId());
        groupModelTimetableList.setName(groupEntityTimetableList.getName());
        groupModelTimetableList.setTimetableList(timetableList);
        return groupModelTimetableList;
    }
}
