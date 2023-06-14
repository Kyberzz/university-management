package ua.com.foxminded.university.service.impl;

import static ua.com.foxminded.university.exception.ServiceErrorCode.*;

import java.lang.reflect.Type;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.comparator.LessonDTOComparator;
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.repository.TimingRepository;
import ua.com.foxminded.university.service.LessonService;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    
    public static final int ONE_WEEK = 1;
    public static final int NO_LESSONS = 0;
    public static final int DEFFALUT_LESSONS_QUANTITY = 5;
    public static final int OFFSET = 1;
    public static final int FIRST_ELEMENT = 0;
    public static final int WEEKS_OFFSET = 3;
    public static final int WEEKS_QUANTITY = 4;
    public static final int END_WEEK_DAY_NUMBER = 7;
    public static final int START_WEEK_DAY_NUMBER = 0;
    public static final int ONE_DAY = 1;
    public static final Type LESSON_LIST_TYPE = 
            new TypeToken<List<LessonDTO>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final LessonRepository lessonRepository;
    private final TimingRepository timingRepository;
    private final TimetableRepository timetableRepository;
    private final GroupRepository groupRepository;
    
    @Override
    public List<LessonDTO> getLessonsByTeacherId(int teacherId) {
        try {
            List<Lesson> lessons = lessonRepository.findByTeacherId(teacherId);
            return modelMapper.map(lessons, LESSON_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(LESSONS_FETCH_ERROR, e);
        }
    }
    
    @Override
    public Set<LessonDTO> sortByDatestamp(Set<LessonDTO> lessons) {
        LessonDTOComparator comparator = new LessonDTOComparator();
        List<LessonDTO> list = new ArrayList<>(lessons);
        Collections.sort(list, comparator);
        return new LinkedHashSet<>(list);
    }
    
    @Override
    public LocalDate moveWeekBack(LocalDate date) {
        return date.minusWeeks(ONE_WEEK);
    }

    @Override
    public LocalDate moveWeekForward(LocalDate date) {
        return date.plusWeeks(ONE_WEEK);
    }
    
    @Override
    public List<List<LessonDTO>> getWeekLessonsOwnedByTeacher(LocalDate date, int teacherId) {
        
        LocalDate monday = findMondayOfWeek(date);
        int lessonsQuantity = defineMaxNumberOfDayLessonsInWeekForTeacher(date, teacherId);
        
        if (lessonsQuantity < DEFFALUT_LESSONS_QUANTITY) {
            lessonsQuantity = DEFFALUT_LESSONS_QUANTITY;
        }
        
        List<List<LessonDTO>> lessons = new ArrayList<>();
       
        try {
            for (int i = 0; i < lessonsQuantity; i++) {
                List<LessonDTO> lessonsOfWeekContainingIdemOrder = new ArrayList<>();
                
                for (int j = 0; j < DayOfWeek.values().length; j++) {
                    LocalDate datestamp = monday.plusDays(j);
                    Lesson lesson = lessonRepository
                            .findByDatestampAndTeacherIdAndLessonOrder(datestamp, teacherId, i);
                    if (lesson == null) {
                        lessonsOfWeekContainingIdemOrder.add(LessonDTO.builder()
                                .datestamp(datestamp)
                                .build());
                    } else {
                        LessonDTO lessonDto = modelMapper.map(lesson, LessonDTO.class);
                        lessonsOfWeekContainingIdemOrder.add(lessonDto);  
                    }
                }
                addLessonTiming(lessonsOfWeekContainingIdemOrder);
                lessons.add(lessonsOfWeekContainingIdemOrder);
            }
        } catch (DataAccessException e) {
            throw new ServiceException(LESSON_FETCH_ERROR, e); 
        }
        
        return lessons;
    }
    
    @Override
    public List<LessonDTO> applyTimetable(LocalDate date, int timetableId) {
        Timetable timetable = timetableRepository.findById(timetableId);
        List<Lesson> lessons = lessonRepository.findByDatestamp(date);
        lessons.stream().forEach(lesson -> lesson.setTimetable(timetable));
        try {
            lessonRepository.saveAllAndFlush(lessons);
            return modelMapper.map(lessons, LESSON_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(LESSON_UPDATE_ERROR, e);
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
                    .skip(lesson.getLessonOrder() - OFFSET).findFirst();

            if (timing.isPresent()) {
                lesson.setStartTime(timing.get().getStartTime());
                lesson.setEndTime(timing.get().getStartTime().plus(
                        timing.get().getLessonDuration()));
            }
        }
    }

    @Override
    public LocalDate moveMonthForward(LocalDate date) {
        try {
            return date.plusWeeks(WEEKS_OFFSET);
        } catch (DateTimeException e) {
            throw new ServiceException(API_ERROR, e);
        }
    }

    @Override
    public LocalDate moveMonthBack(LocalDate date) {
        try {
            return date.minusWeeks(WEEKS_OFFSET);
        } catch (DateTimeException e) {
            throw new ServiceException(API_ERROR, e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            lessonRepository.deleteById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new ServiceException(LESSON_DELETE_ERROR, e);
        }
    }

    @Override
    public void update(LessonDTO model) {
        try {
            Lesson entity = modelMapper.map(model, Lesson.class);
            Lesson persistEntity = lessonRepository.findById(model.getId().intValue());
            persistEntity.setCourse(entity.getCourse());
            persistEntity.setDatestamp(entity.getDatestamp());
            persistEntity.setDescription(entity.getDescription());
            persistEntity.setLessonOrder(entity.getLessonOrder());
            persistEntity.setTeacher(entity.getTeacher());
            persistEntity.setTimetable(entity.getTimetable());
            persistEntity.setGroups(entity.getGroups());
            lessonRepository.saveAndFlush(persistEntity);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(LESSON_UPDATE_ERROR, e);
        }
    }

    @Override
    public LessonDTO create(LessonDTO lessonDto) {
        try {
            Lesson persistedLesson = lessonRepository.findByDatestampAndLessonOrderAndGroupsId(
                    lessonDto.getDatestamp(),
                    lessonDto.getLessonOrder(), 
                    lessonDto.getGroups().iterator().next().getId());
            
            Lesson counterpartLesson = lessonRepository
                    .findByDatestampAndTeacherIdAndLessonOrderAndCourseId(
                            lessonDto.getDatestamp(),
                            lessonDto.getTeacher().getId(),
                            lessonDto.getLessonOrder(),
                            lessonDto.getCourse().getId());
            
            Lesson lesson = modelMapper.map(lessonDto, Lesson.class);
            
            if (persistedLesson == null && counterpartLesson == null) {
                Lesson createdEntity = lessonRepository.saveAndFlush(lesson);
                return modelMapper.map(createdEntity, LessonDTO.class);
            } else if (persistedLesson == null) {
                int groupId = lesson.getGroups().iterator().next().getId();
                Group group = groupRepository.findById(groupId); 
                counterpartLesson.addGroup(group);
                Lesson updatedLesson = lessonRepository.saveAndFlush(counterpartLesson);
                return modelMapper.map(updatedLesson, LessonDTO.class);
            } else {
                return modelMapper.map(persistedLesson, LessonDTO.class);
            }
        } catch (DataAccessException | IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException(LESSON_PERSISTENCE_ERROR, e);
        }
    }

    @Override
    public LessonDTO getById(int id) {
        try {
            Lesson entity = lessonRepository.findById(id);
            return modelMapper.map(entity, LessonDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(LESSON_FETCH_ERROR, e);
        }
    }

    @Override
    public List<List<List<LessonDTO>>> getMonthLessons(LocalDate date) {

        List<List<List<LessonDTO>>> monthTimetable = new ArrayList<>();

        for (int i = 0; i < WEEKS_QUANTITY; i++) {
            LocalDate datestamp = date.plusWeeks(i);
            List<List<LessonDTO>> weekTimetables = getWeekLessons(datestamp);
            monthTimetable.add(weekTimetables);
        }
        return monthTimetable;
    }

    @Override
    public List<LessonDTO> getDayLessons(LocalDate date) {
        try {
            List<Lesson> entities = lessonRepository.findByDatestamp(date);
            List<LessonDTO> models = modelMapper.map(entities, LESSON_LIST_TYPE);

            if (models.isEmpty()) {
                models = new ArrayList<>();
                LessonDTO model = new LessonDTO();
                model.setDatestamp(date);
                models.add(model);
            }

            return models;
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(LESSON_FETCH_ERROR, e);
        }
    }

    @Override
    public List<LessonDTO> getAll() {
        try {
            List<Lesson> timetableEntities = lessonRepository.findAll();
            return modelMapper.map(timetableEntities, LESSON_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(LESSONS_FETCH_ERROR, e);
        }
    }
    
    private int defineMaxNumberOfDayLessonsInWeekForTeacher(LocalDate datestamp, 
            int teacherId) {
        
        List<List<Lesson>> weekLessons = new ArrayList<>();
        
        try {
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                List<Lesson> lessons = lessonRepository.findByDatestampAndTeacherId(
                        datestamp, teacherId);
                weekLessons.add(lessons);
            }
        } catch (DataAccessException e) {
            throw new ServiceException(LESSONS_FETCH_ERROR, e);
        }
        
        Optional<Integer> lessonsNumber = weekLessons.stream()
                .map(lessons -> lessons.size()).max(Integer::compareTo);
        
        if (lessonsNumber.isPresent()) {
            return lessonsNumber.get();
        } else {
            return NO_LESSONS;
        }
    }
    
    private List<List<LessonDTO>> getWeekLessons(LocalDate date) {

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
