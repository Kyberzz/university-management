package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.repository.TimingRepository;
import ua.com.foxminded.university.service.LessonService;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    
    public static final int DEFFALUT_LESSONS_QUANTITY = 5;
    public static final int OFFSET = 1;
    public static final int FIRST_ELEMENT = 0;
    public static final int ARRAY_INDEX_OFFSET = 1;
    public static final int WEEKS_OFFSET = 3;
    public static final int WEEKS_QUANTITY = 4;
    public static final int END_WEEK_DAY_NUMBER = 7;
    public static final int START_WEEK_DAY_NUMBER = 0;
    public static final int ONE_DAY = 1;
    public static final Type LESSON_MODELS_LIST_TYPE = 
            new TypeToken<List<LessonDTO>>() {}.getType();
    
            private final ModelMapper modelMapper;
    private final LessonRepository lessonRepository;
    private final TimingRepository timingRepository;
    private final TimetableRepository timetableRepository;
    
    @Override
    public List<List<LessonDTO>> getWeekLessonsOwnedByTeacher(LocalDate date, String email) 
            throws ServiceException {
        List<List<LessonDTO>> teacherLessons = getWeekLessons(date).stream().map(dayLessons -> dayLessons.stream()
                .filter(lesson -> {
                    if (lesson.hasTeacher()) {
                        return lesson.getTeacher().getUser().getEmail().equals(email);
                    } else {
                        return false;
                    }
                }).collect(Collectors.toList())).collect(Collectors.toList());
//        addEmptyLessons(teacherLessons);
        return teacherLessons;
    }
    
    @Override
    public List<LessonDTO> applyTimetable(LocalDate date, int timetableId) 
            throws ServiceException {
        Timetable timetable = timetableRepository.findById(timetableId);
        List<Lesson> lessons = lessonRepository.findByDatestamp(date);
        lessons.stream().forEach(lesson -> lesson.setTimetable(timetable));
        try {
            lessonRepository.saveAllAndFlush(lessons);
            return modelMapper.map(lessons, LESSON_MODELS_LIST_TYPE);
        } catch (DataAccessException e) {
            throw new ServiceException("Applying timetable for lessons fails", e);
        }
    }

    @Override
    public void sortByLessonOrder(List<LessonDTO> lessons) {
        Collections.sort(lessons, Comparator.comparing(LessonDTO::getLessonOrder));
    }

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
                    .skip(lesson.getLessonOrder() - ARRAY_INDEX_OFFSET).findFirst();

            if (timing.isPresent()) {
                lesson.setStartTime(timing.get().getStartTime());
                lesson.setEndTime(timing.get().getStartTime().plus(timing.get().getLessonDuration()));
            }
        }
    }

    @Override
    public LocalDate moveForward(LocalDate date) {
        return date.plusWeeks(WEEKS_OFFSET);
    }

    @Override
    public LocalDate moveBack(LocalDate date) {
        return date.minusWeeks(WEEKS_OFFSET);
    }

    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            lessonRepository.deleteById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new ServiceException("Deleting timetable with id = " + id + "", e);
        }
    }

    @Override
    public void update(LessonDTO model) throws ServiceException {
        try {
            Lesson entity = modelMapper.map(model, Lesson.class);
            Lesson persistEntity = lessonRepository.findById(model.getId().intValue());
            persistEntity.setCourse(entity.getCourse());
            persistEntity.setDatestamp(entity.getDatestamp());
            persistEntity.setDescription(entity.getDescription());
            persistEntity.setGroups(entity.getGroups());
            lessonRepository.saveAndFlush(persistEntity);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Updating timetable failes", e);
        }
    }

    @Override
    public LessonDTO create(LessonDTO dto) throws ServiceException {
        try {
            Lesson persistEntity = lessonRepository.findByDatestampAndGroupIdAndTimingId(
                    dto.getDatestamp(),
                    dto.getLessonOrder(), dto.getGroup().getId());

            if (persistEntity == null) {
                Lesson entity = modelMapper.map(dto, Lesson.class);
                Lesson createdEntity = lessonRepository.saveAndFlush(entity);
                return modelMapper.map(createdEntity, LessonDTO.class);
            } else {
                dto.setId(persistEntity.getId());
                update(dto);
                return dto;
            }
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Creating a timetable fails", e);
        }
    }

    @Override
    public LessonDTO getById(int id) throws ServiceException {
        try {
            Lesson entity = lessonRepository.findById(id);
            return modelMapper.map(entity, LessonDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetable by ID fails", e);
        }
    }

    @Override
    public List<List<List<LessonDTO>>> getMonthLessons(LocalDate date) 
            throws ServiceException {

        List<List<List<LessonDTO>>> monthTimetable = new ArrayList<>();

        for (int i = 0; i < WEEKS_QUANTITY; i++) {
            LocalDate datestamp = date.plusWeeks(i);
            List<List<LessonDTO>> weekTimetables = getWeekLessons(datestamp);
            monthTimetable.add(weekTimetables);
        }
        return monthTimetable;
    }

    @Override
    public List<LessonDTO> getDayLessons(LocalDate date) throws ServiceException {
        try {
            List<Lesson> entities = lessonRepository.findByDatestamp(date);
            List<LessonDTO> models = modelMapper.map(entities, LESSON_MODELS_LIST_TYPE);

            if (models.isEmpty()) {
                models = new ArrayList<>();
                LessonDTO model = new LessonDTO();
                model.setDatestamp(date);
                models.add(model);
            }

            return models;
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetables of day fails", e);
        }
    }

    @Override
    public List<LessonDTO> getAll() throws ServiceException {
        try {
            List<Lesson> timetableEntities = lessonRepository.findAll();
            return modelMapper.map(timetableEntities, LESSON_MODELS_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all timetables was failed", e);
        }
    }
    
    private void addEmptyLessons(List<List<LessonDTO>> weekLessons) {
        List<Timing> timings = getLessonTimingsOfBiggestDayOfWeek(weekLessons);
        
        int dayLessonsQuantity;
        
        if (timings == null) {
            dayLessonsQuantity = DEFFALUT_LESSONS_QUANTITY;
        } else {
            dayLessonsQuantity = timings.size();
        }
        
        for(List<LessonDTO> dayLessons : weekLessons) {
            int emptyLessonsNumber = dayLessonsQuantity - dayLessons.size();
            
            if (emptyLessonsNumber != 0) {
                for (int i = 0; i < emptyLessonsNumber; i++) {
                    dayLessons.add(LessonDTO.builder().build());
                }
            }
        }
        
        addLessonOrder(weekLessons);
    }
    
    private void addLessonOrder(List<List<LessonDTO>> weekLessons) {
        weekLessons.stream().forEach(dayLessons -> {
            IntStream.range(0, dayLessons.size()).forEach(number -> {
                Optional<LessonDTO> lessonWithOrder = dayLessons.stream().filter(
                        lesson -> lesson.getLessonOrder() == (number + OFFSET)).findFirst();
                
                if (!lessonWithOrder.isPresent()) {
                    Optional<LessonDTO> optionalLesson = dayLessons.stream().filter(
                            lesson -> lesson.getLessonOrder() == null).findFirst();
                    
                    if(optionalLesson.isPresent()) {
                        optionalLesson.get().setLessonOrder(number);
                    }
                }
            });
        });
    }
    
    private  List<Timing> getLessonTimingsOfBiggestDayOfWeek(
            List<List<LessonDTO>> weekLessons) {
       
        List<List<Timing>> weekTimings = new ArrayList<>();

        for (List<LessonDTO> dayLessons : weekLessons) {
            if (dayLessons != null) {
                List<Timing> dayTimings = timingRepository.findByTimetableId(
                        dayLessons.get(FIRST_ELEMENT).getTimetable().getId());
                weekTimings.add(dayTimings);
            }
        }

        Optional<List<Timing>> optionalTimings = weekTimings.stream().max(
                Comparator.comparing(List::size));
        
        List<Timing> timings;
        
        if (optionalTimings.isPresent()) {
            timings = optionalTimings.get();
        } else {
            timings = new ArrayList<>();
        }
        return timings;
    }

    private List<List<LessonDTO>> getWeekLessons(LocalDate date) throws ServiceException {

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
