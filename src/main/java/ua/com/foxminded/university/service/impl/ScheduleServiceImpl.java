package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.entity.ScheduleEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.ScheduleModel;
import ua.com.foxminded.university.repository.ScheduleRepository;
import ua.com.foxminded.university.service.ScheduleService;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    
    public static final int OFFSET_WEEKS_QUANTITY = 3;
    public static final int WEEKS_QUANTITY = 4;
    public static final int END_WEEK_DAY_NUMBER = 7;
    public static final int START_WEEK_DAY_NUMBER = 0;
    public static final int ONE_DAY = 1;
    public static final Type SCHEDULE_MODEL_LIST_TYPE = 
            new TypeToken<List<ScheduleModel>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final ScheduleRepository scheduleRepository;
    
    @Override
    public LocalDate moveForward(LocalDate date) {
        return date.plusWeeks(OFFSET_WEEKS_QUANTITY);
    }

    @Override
    public LocalDate moveBack(LocalDate date) {
        return date.minusWeeks(OFFSET_WEEKS_QUANTITY);
    }
    
    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            scheduleRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting timetable with id = " + id +"", e);
        }
    }

    @Override
    public void update(ScheduleModel model) throws ServiceException {
        try {
            ScheduleEntity entity = modelMapper.map(model, ScheduleEntity.class);
            ScheduleEntity persistEntity = scheduleRepository.findById(
                    model.getId().intValue());
            persistEntity.setCourse(entity.getCourse());
            persistEntity.setDatestamp(entity.getDatestamp());
            persistEntity.setDescription(entity.getDescription());
            persistEntity.setGroup(entity.getGroup());
            scheduleRepository.saveAndFlush(persistEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating timetable failes", e);
        }
    }
    
    @Override
    public void create(ScheduleModel model) throws ServiceException {
        try {
//            ScheduleEntity persistEntity = scheduleRepository
//                    .findByDatestampAndLessonOrderAndGroupId(model.getDatestamp(), 
//                                                             model.getLessonOrder(), 
//                                                             model.getGroup().getId());
            
//            if (persistEntity == null) {
//                ScheduleEntity entity = modelMapper.map(model, ScheduleEntity.class);
//                scheduleRepository.saveAndFlush(entity);
//            } else {
//                model.setId(persistEntity.getId());
//                update(model);
//            }
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating a timetable fails", e);
        }
    }

    @Override
    public ScheduleModel getById(int id) throws ServiceException {
        try {
            ScheduleEntity entity = scheduleRepository.findById(id);
            return modelMapper.map(entity, ScheduleModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetable by ID fails", e);
        }
    }
    
    @Override
    public List<List<List<ScheduleModel>>> getMonthSchedule(LocalDate date) 
            throws ServiceException {
        
        List<List<List<ScheduleModel>>> monthTimetable = new ArrayList<>();
        
        for(int i = 0; i < WEEKS_QUANTITY; i++) {
            LocalDate datestamp = date.plusWeeks(i);
            List<List<ScheduleModel>> weekTimetables = getWeekTimetable(datestamp);
            monthTimetable.add(weekTimetables);
        }
        return monthTimetable;
    }
    
    
    @Override
    public List<ScheduleModel> getDaySdhedule(LocalDate date) throws ServiceException {
        try {
            List<ScheduleEntity> entities = scheduleRepository.findByDatestamp(date);
            List<ScheduleModel> models =  modelMapper.map(entities, SCHEDULE_MODEL_LIST_TYPE);

            if (models.isEmpty()) {
                models = new ArrayList<>();
                ScheduleModel model = new ScheduleModel();
                model.setDatestamp(date);
                models.add(model);
            }
            return models;
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetables of day fails", e);
        }
    }
    
    @Override
    public List<ScheduleModel> getAll() throws ServiceException {
        try {
            List<ScheduleEntity> timetableEntities = scheduleRepository.findAll();
            return modelMapper.map(timetableEntities, SCHEDULE_MODEL_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all timetables was failed", e);
        }
    }
    
    private List<List<ScheduleModel>> getWeekTimetable(LocalDate date) 
            throws ServiceException {
        
        LocalDate startDayOfWeek = findMondayOfWeek(date);
        List<List<ScheduleModel>> weekTimetables = new ArrayList<>();
        List<ScheduleModel> dayTimetables;
        
        for (int i = 0; i < DayOfWeek.values().length; i++) {
            dayTimetables = getDaySdhedule(startDayOfWeek.plusDays(i));
            weekTimetables.add(dayTimetables);
        }
        return weekTimetables;
    }
    
    private LocalDate findMondayOfWeek(LocalDate date) {
        LocalDate startDayOfWeek = date;
        
        if (startDayOfWeek.getDayOfWeek() == DayOfWeek.MONDAY) {
            return startDayOfWeek;
        } else {
            while (startDayOfWeek.getDayOfWeek() != DayOfWeek.MONDAY) {
                startDayOfWeek = startDayOfWeek.minusDays(ONE_DAY);
            }
            return startDayOfWeek;
        }
    }
}