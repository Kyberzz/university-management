package ua.com.foxminded.university.service.impl;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.entitymother.TimetableMother.TIMETABLE_NAME;
import static ua.com.foxminded.university.service.impl.TimetableServiceIml.TIMETABLES_LIST_TYPE;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ua.com.foxminded.university.dto.TimetableDTO;
import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entitymother.TimetableMother;
import ua.com.foxminded.university.repository.TimetableRepository;

@ExtendWith(MockitoExtension.class)
class TimetableServiceImplTest {
    
    public static final int TIMETABLE_ID = 1;
    
    @InjectMocks
    private TimetableServiceIml timetableService;
    
    @Mock
    private TimetableRepository timetableRepositoryMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private Timetable timetable;
    private List<Timetable> timetablesList;
    private TimetableDTO timetableDto;
    
    @BeforeEach
    void init() {
        timetable = TimetableMother.complete().build();
        timetablesList = asList(timetable);
        timetableDto = TimetableDTO.builder().name(TIMETABLE_NAME).build();
    }
    
    @Test
    void sortByName_ShouldSort() {
        TimetableDTO timetableOne = TimetableDTO.builder().name("A").build();
        TimetableDTO timetableTwo = TimetableDTO.builder().name("B").build();
        List<TimetableDTO> timetables = Arrays.asList(timetableTwo, timetableOne);
        timetableService.sortByName(timetables);
        assertEquals(timetableOne, timetables.iterator().next());
    }
    
    @Test
    void sortTimingsByStartTime_ShouldSortTimings() {
        TimingDTO timingOne = TimingDTO.builder().startTime(LocalTime.of(1, 0)).build();
        TimingDTO timingTwo = TimingDTO.builder().startTime(LocalTime.of(2, 0)).build();
        TimetableDTO timetable = TimetableDTO.builder().timings(new HashSet<>()).build();
        timetable.getTimings().add(timingTwo);
        timetable.getTimings().add(timingOne);
        
        timetableService.sortTimingsByStartTime(timetable);
        assertEquals(timingOne, timetable.getTimings().iterator().next());
    }
    
    @Test
    void getByIdWithTimings() {
        when(timetableRepositoryMock.getByIdWithTimings(anyInt())).thenReturn(timetable);
        timetableService.getByIdWithTimings(TIMETABLE_ID);
        verify(timetableRepositoryMock).getByIdWithTimings(anyInt());
        verify(modelMapperMock).map(timetable, TimetableDTO.class);
    }
    
    @Test
    void getAllWithTimings_ShouldReturnTimetableWhithRelatedTimings() {
        when(timetableRepositoryMock.getAllWithTimings()).thenReturn(timetablesList);
        timetableService.getAllWithTimings();
        verify(timetableRepositoryMock).getAllWithTimings();
        verify(modelMapperMock).map(timetablesList, TIMETABLES_LIST_TYPE);
    }
    
    @Test
    void deleteById_ShouldDeleteTeacher() {
        timetableService.deleteById(TIMETABLE_ID);
        verify(timetableRepositoryMock).deleteById(anyInt());
    }
    
    @Test
    void create_ShouldCreateTimetable() {
        when(modelMapperMock.map(timetableDto, Timetable.class)).thenReturn(timetable);
        when(timetableRepositoryMock.saveAndFlush(timetable)).thenReturn(timetable);
        timetableService.create(timetableDto);
        
        verify(modelMapperMock).map(timetableDto, Timetable.class);
        verify(timetableRepositoryMock).saveAndFlush(ArgumentMatchers.isA(Timetable.class));
        verify(modelMapperMock).map(timetable, TimetableDTO.class);
    }
    
    @Test
    void update_ShouldUpdateTimetable() {
        timetableDto.setId(TIMETABLE_ID);
        when(timetableRepositoryMock.findById(anyInt())).thenReturn(timetable);
        when(modelMapperMock.map(timetableDto, Timetable.class)).thenReturn(timetable);
        timetableService.update(timetableDto);
        verify(modelMapperMock).map(timetableDto, Timetable.class);
        verify(timetableRepositoryMock).saveAndFlush(isA(Timetable.class));
    }
    
    @Test
    void getById_ShouldReturnTeacher() {
        when(timetableRepositoryMock.findById(anyInt())).thenReturn(timetable);
        timetableService.getById(TIMETABLE_ID);
        verify(modelMapperMock).map(timetable, TimetableDTO.class);
    }

    @Test
    void getByCoursesId_ShouldReturnTeachersList() {
        when(timetableRepositoryMock.findAll()).thenReturn(timetablesList);
        timetableService.getAll();
        
        verify(timetableRepositoryMock).findAll();
        verify(modelMapperMock).map(timetablesList, TIMETABLES_LIST_TYPE);
    }
}
