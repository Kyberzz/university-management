package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.TimingRepository;
import ua.com.foxminded.university.service.LessonService;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    
    public static final int ARRAY_INDEX_OFFSET = 1;
    public static final int OFFSET_WEEKS_QUANTITY = 3;
    public static final int WEEKS_QUANTITY = 4;
    public static final int END_WEEK_DAY_NUMBER = 7;
    public static final int START_WEEK_DAY_NUMBER = 0;
    public static final int ONE_DAY = 1;
    public static final Type LESSON_MODELS_LIST_TYPE = 
            new TypeToken<List<LessonDTO>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final LessonRepository lessonRepository;
    private final TimingRepository timingRepository;
    
    @Override
    public void addLessonTiming(List<LessonDTO> lessons) {
        lessons.stream().forEach(this::addLessonTiming);
    }
   
    @Override
    public void addLessonTiming(LessonDTO lesson) {
        if (lesson.hasTimetable()) {
            List<Timing> timings = timingRepository.findByTimetableId(
                    lesson.getTimetable().getId());
            Optional<Timing> timing = timings.stream()
                    .sorted(Comparator.comparing(Timing::getStartTime))
                    .skip(lesson.getLessonOrder() - ARRAY_INDEX_OFFSET)
                    .findFirst();
           
            if (timing.isPresent()) {
                lesson.setStartTime(timing.get().getStartTime());
                lesson.setEndTime(timing.get().getStartTime().plus(
                        timing.get().getLessonDuration()));
            }
        }
    }
    
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
            lessonRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting timetable with id = " + id +"", e);
        }
    }

    @Override
    public void update(LessonDTO model) throws ServiceException {
        try {
            Lesson entity = modelMapper.map(model, Lesson.class);
            Lesson persistEntity = lessonRepository.findById(
                    model.getId().intValue());
            persistEntity.setCourse(entity.getCourse());
            persistEntity.setDatestamp(entity.getDatestamp());
            persistEntity.setDescription(entity.getDescription());
            persistEntity.setGroup(entity.getGroup());
            lessonRepository.saveAndFlush(persistEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating timetable failes", e);
        }
    }
    
    @Override
    public void create(LessonDTO model) throws ServiceException {
        try {
            Lesson persistEntity = lessonRepository
                    .findByDatestampAndGroupIdAndTimingId(model.getDatestamp(), 
                                                          model.getLessonOrder(),
                                                          model.getGroup().getId());
            
            if (persistEntity == null) {
                Lesson entity = modelMapper.map(model, Lesson.class);
                lessonRepository.saveAndFlush(entity);
            } else {
                model.setId(persistEntity.getId());
                update(model);
            }
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating a timetable fails", e);
        }
    }

    @Override
    public LessonDTO getById(int id) throws ServiceException {
        try {
            Lesson entity = lessonRepository.findById(id);
            return modelMapper.map(entity, LessonDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetable by ID fails", e);
        }
    }
    
    @Override
    public List<List<List<LessonDTO>>> getMonthLessons(LocalDate date) 
            throws ServiceException {
        
        List<List<List<LessonDTO>>> monthTimetable = new ArrayList<>();
        
        for(int i = 0; i < WEEKS_QUANTITY; i++) {
            LocalDate datestamp = date.plusWeeks(i);
            List<List<LessonDTO>> weekTimetables = getWeekTimetable(datestamp);
            monthTimetable.add(weekTimetables);
        }
        return monthTimetable;
    }
    
    @Override
    public List<LessonDTO> getDayLessons(LocalDate date) throws ServiceException {
        try {
            List<Lesson> entities = lessonRepository.findByDatestamp(date);
            List<LessonDTO> models =  modelMapper.map(entities, LESSON_MODELS_LIST_TYPE);

            if (models.isEmpty()) {
                models = new ArrayList<>();
                LessonDTO model = new LessonDTO();
                model.setDatestamp(date);
                models.add(model);
            }
            
            return models;
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetables of day fails", e);
        }
    }
    
    @Override
    public List<LessonDTO> getAll() throws ServiceException {
        try {
            List<Lesson> timetableEntities = lessonRepository.findAll();
            return modelMapper.map(timetableEntities, LESSON_MODELS_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all timetables was failed", e);
        }
    }
    
    private List<List<LessonDTO>> getWeekTimetable(LocalDate date) 
            throws ServiceException {
        
        LocalDate startDayOfWeek = findMondayOfWeek(date);
        List<List<LessonDTO>> weekTimetables = new ArrayList<>();
        List<LessonDTO> dayTimetables;
        
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
