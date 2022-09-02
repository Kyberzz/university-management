package ua.com.foxminded.university.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.model.WeekDayModel;

@ExtendWith(MockitoExtension.class)
class TimetableServiceImplTestTestTest {
    
    @InjectMocks
    private TimetableServiceImpl timetableService;
    
    @Mock
    private TimetableDao timetableDao;

    @Test
    void updateTimetable_CallingDaoObject_CorrectCallQuantity() {
        TimetableModel timetableModel = new TimetableModel();
        timetableModel.setCourse(new CourseModel());
        timetableModel.setGroup(new GroupModel());
        timetableModel.setWeekDay(WeekDayModel.FRIDAY);
        timetableService.updateTimetable(timetableModel);
        verify(timetableDao, times(1)).update(ArgumentMatchers.<TimetableEntity>any());
    }
}
