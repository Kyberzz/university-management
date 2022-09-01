package ua.com.foxminded.university.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.model.WeekDayModel;

@ExtendWith(MockitoExtension.class)
class TimetableServiceImplTestTestTest {
    
    private static final String DESCRTIPTION = "the timetable description";
    private static final long START_TIME = 7097;
    private static final long END_TIME = 14646;
    
    private static final int TIMETABLE_ID = 2;
    private static final int GROUP_ID = 2;
    private static final int COURSE_ID = 1;
    
    @InjectMocks
    private TimetableServiceImpl timetableService;
    
    @Mock
    private TimetableDao timetableDao;

    @Test
    void updateTimetable_CallingDaoObject_CorrectCallQuantity() {
        TimetableModel timetableModel = new TimetableModel();
        timetableModel.setCourse(new CourseModel(COURSE_ID));
        timetableModel.setDescription(DESCRTIPTION);
        timetableModel.setEndTime(END_TIME);
        timetableModel.setGroup(new GroupModel(GROUP_ID));
        timetableModel.setId(TIMETABLE_ID);
        timetableModel.setStartTime(START_TIME);
        timetableModel.setWeekDay(WeekDayModel.valueOf(WEDNESDAY));
        
    }
}
