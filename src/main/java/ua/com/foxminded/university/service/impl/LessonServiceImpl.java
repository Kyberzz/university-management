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
import ua.com.foxminded.university.entity.LessonEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.LessonModel;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.service.LessonService;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    
    public static final int OFFSET_WEEKS_QUANTITY = 3;
    public static final int WEEKS_QUANTITY = 4;
    public static final int END_WEEK_DAY_NUMBER = 7;
    public static final int START_WEEK_DAY_NUMBER = 0;
    public static final int ONE_DAY = 1;
    public static final Type SCHEDULE_MODEL_LIST_TYPE = 
            new TypeToken<List<LessonModel>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final LessonRepository scheduleRepository;
    
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
    public void update(LessonModel model) throws ServiceException {
        try {
            LessonEntity entity = modelMapper.map(model, LessonEntity.class);
            LessonEntity persistEntity = scheduleRepository.findById(
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
    public void create(LessonModel model) throws ServiceException {
        try {
            LessonEntity persistEntity = scheduleRepository
                    .findByDatestampAndGroupIdAndTimingId(model.getDatestamp(), 
                                                          model.getGroup().getId(),
                                                          model.getTiming().getId());
            
            if (persistEntity == null) {
                LessonEntity entity = modelMapper.map(model, LessonEntity.class);
                scheduleRepository.saveAndFlush(entity);
            } else {
                model.setId(persistEntity.getId());
                update(model);
            }
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating a timetable fails", e);
        }
    }

    @Override
    public LessonModel getById(int id) throws ServiceException {
        try {
            LessonEntity entity = scheduleRepository.findById(id);
            return modelMapper.map(entity, LessonModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetable by ID fails", e);
        }
    }
    
    @Override
    public List<List<List<LessonModel>>> getMonthLessons(LocalDate date) 
            throws ServiceException {
        
        List<List<List<LessonModel>>> monthTimetable = new ArrayList<>();
        
        for(int i = 0; i < WEEKS_QUANTITY; i++) {
            LocalDate datestamp = date.plusWeeks(i);
            List<List<LessonModel>> weekTimetables = getWeekTimetable(datestamp);
            monthTimetable.add(weekTimetables);
        }
        return monthTimetable;
    }
    
    
    @Override
    public List<LessonModel> getDayLessons(LocalDate date) throws ServiceException {
        try {
            List<LessonEntity> entities = scheduleRepository.findByDatestamp(date);
            List<LessonModel> models =  modelMapper.map(entities, SCHEDULE_MODEL_LIST_TYPE);

            if (models.isEmpty()) {
                models = new ArrayList<>();
                LessonModel model = new LessonModel();
                model.setDatestamp(date);
                models.add(model);
            }
            return models;
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetables of day fails", e);
        }
    }
    
    @Override
    public List<LessonModel> getAll() throws ServiceException {
        try {
            List<LessonEntity> timetableEntities = scheduleRepository.findAll();
            return modelMapper.map(timetableEntities, SCHEDULE_MODEL_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all timetables was failed", e);
        }
    }
    
    private List<List<LessonModel>> getWeekTimetable(LocalDate date) 
            throws ServiceException {
        
        LocalDate startDayOfWeek = findMondayOfWeek(date);
        List<List<LessonModel>> weekTimetables = new ArrayList<>();
        List<LessonModel> dayTimetables;
        
        for (int i = 0; i < DayOfWeek.values().length; i++) {
            dayTimetables = getDayLessons(startDayOfWeek.plusDays(i));
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
