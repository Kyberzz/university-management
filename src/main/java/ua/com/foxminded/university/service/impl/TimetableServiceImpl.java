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
    
    public static final int WEEKS_QUANTITY = 4;
    public static final int END_WEEK_DAY_NUMBER = 7;
    public static final int START_WEEK_DAY_NUMBER = 0;
    public static final int ONE_DAY = 1;

    private Type listType = new TypeToken<List<TimetableModel>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final TimetableRepository timetableRepository;
    
    @Override
    public List<List<List<TimetableModel>>> getMonthTimetables(LocalDate date) 
            throws ServiceException {
        
        List<List<List<TimetableModel>>> monthTimetables = new ArrayList<>();
        
        for(int i = 0; i < WEEKS_QUANTITY; i++) {
            LocalDate datestamp = date.plusWeeks(i);
            List<List<TimetableModel>> weekTimetables = getWeekTimetables(datestamp);
            monthTimetables.add(weekTimetables);
        }
        return monthTimetables;
    }
    
    @Override
    public List<List<TimetableModel>> getWeekTimetables(LocalDate date) 
            throws ServiceException {
        
        LocalDate startDayOfWeek = findMondayOfWeek(date);
        List<List<TimetableModel>> weekTimetables = new ArrayList<>();
        List<TimetableModel> dayTimetables;
        
        for (int i = 0; i < DayOfWeek.values().length; i++) {
            dayTimetables = getDayTimetalbes(startDayOfWeek.plusDays(i));
            weekTimetables.add(dayTimetables);
        }
        return weekTimetables;
    }
    
    @Override
    public List<TimetableModel> getDayTimetalbes(LocalDate date) throws ServiceException {
        try {
            List<TimetableEntity> entities = timetableRepository.findByDatestamp(date);
            List<TimetableModel> models =  modelMapper.map(entities, listType);

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
            return modelMapper.map(timetableEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all timetables was failed", e);
        }
    }
    
    @Override
    public void updateTimetable(TimetableModel timetableModel) throws ServiceException {
        try {
            TimetableEntity timetableEntity = modelMapper.map(timetableModel, 
                                                              TimetableEntity.class);
            timetableRepository.save(timetableEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating the timetable failed.", e);
        }
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
