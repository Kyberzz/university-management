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
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.service.TimetableService;

@Service
@Transactional
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {
    
    public static final int OFFSET_WEEKS_QUANTITY = 3;
    public static final int WEEKS_QUANTITY = 4;
    public static final int END_WEEK_DAY_NUMBER = 7;
    public static final int START_WEEK_DAY_NUMBER = 0;
    public static final int ONE_DAY = 1;
    public static final Type TIMETABEL_MODEL_LIST_TYPE = 
            new TypeToken<List<TimetableModel>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final TimetableRepository timetableRepository;
    
    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            timetableRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting timetable with id = " + id +"", e);
        }
    }

    @Override
    public void update(TimetableModel model) throws ServiceException {
        try {
            TimetableEntity entity = modelMapper.map(model, TimetableEntity.class);
            TimetableEntity persistEntity = timetableRepository.findById(
                    model.getId().intValue());
            persistEntity.setBreakDuration(entity.getBreakDuration());
            persistEntity.setCourse(entity.getCourse());
            persistEntity.setDatestamp(entity.getDatestamp());
            persistEntity.setDescription(entity.getDescription());
            persistEntity.setGroup(entity.getGroup());
            persistEntity.setStartTime(entity.getStartTime());
            timetableRepository.saveAndFlush(persistEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating timetable failes", e);
        }
    }
    
    @Override
    public void create(TimetableModel model) throws ServiceException {
        try {
            TimetableEntity entity = modelMapper.map(model, TimetableEntity.class);
            timetableRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating a timetable fails", e);
        }
    }

    @Override
    public TimetableModel getById(int id) throws ServiceException {
        try {
            TimetableEntity entity = timetableRepository.findById(id);
            return modelMapper.map(entity, TimetableModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetable by ID fails", e);
        }
    }
    
    @Override
    public void moveBackDatestamp(TimetableModel timetable) {
        LocalDate previousMonthDatestamp = timetable.getDatestamp()
                                                    .minusWeeks(OFFSET_WEEKS_QUANTITY);
        timetable.setDatestamp(previousMonthDatestamp);
    }
    
    @Override
    public void moveForwardDatestamp(TimetableModel timetable) {
        LocalDate nextMonthDatestamp = timetable.getDatestamp()
                                                .plusWeeks(OFFSET_WEEKS_QUANTITY);
        timetable.setDatestamp(nextMonthDatestamp);
    }
    
    @Override
    public List<List<List<TimetableModel>>> getMonthTimetable(LocalDate date) 
            throws ServiceException {
        
        List<List<List<TimetableModel>>> monthTimetable = new ArrayList<>();
        
        for(int i = 0; i < WEEKS_QUANTITY; i++) {
            LocalDate datestamp = date.plusWeeks(i);
            List<List<TimetableModel>> weekTimetables = getWeekTimetable(datestamp);
            monthTimetable.add(weekTimetables);
        }
        return monthTimetable;
    }
    
    
    @Override
    public List<TimetableModel> getDayTimetalbe(LocalDate date) throws ServiceException {
        try {
            List<TimetableEntity> entities = timetableRepository.findByDatestamp(date);
            List<TimetableModel> models =  modelMapper.map(entities, TIMETABEL_MODEL_LIST_TYPE);

            if (models.isEmpty()) {
                models = new ArrayList<>();
                TimetableModel model = new TimetableModel();
                model.setDatestamp(date);
                models.add(model);
            }
            return models;
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetables of day fails", e);
        }
    }
    
    @Override
    public List<TimetableModel> getAll() throws ServiceException {
        try {
            List<TimetableEntity> timetableEntities = timetableRepository.findAll();
            return modelMapper.map(timetableEntities, TIMETABEL_MODEL_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all timetables was failed", e);
        }
    }
    
    private List<List<TimetableModel>> getWeekTimetable(LocalDate date) 
            throws ServiceException {
        
        LocalDate startDayOfWeek = findMondayOfWeek(date);
        List<List<TimetableModel>> weekTimetables = new ArrayList<>();
        List<TimetableModel> dayTimetables;
        
        for (int i = 0; i < DayOfWeek.values().length; i++) {
            dayTimetables = getDayTimetalbe(startDayOfWeek.plusDays(i));
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