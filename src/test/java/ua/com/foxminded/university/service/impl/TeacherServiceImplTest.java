package ua.com.foxminded.university.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.service.impl.CourseServiceImplTest.COURSE_ID;
import static ua.com.foxminded.university.service.impl.TeacherServiceImpl.TEACHER_DTO_LIST_TYPE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ua.com.foxminded.university.dto.UserPersonDTO;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.entity.Teacher;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.UserMother;
import ua.com.foxminded.university.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {
    
    public static final String SECOND_TEACHER_LAST_NAME = "B";
    public static final String FIRST_TEACHER_LAST_NAME = "A";
    public static final int TEACHER_ID = 1;
    
    @InjectMocks
    private TeacherServiceImpl teacherService;
    
    @Mock
    private TeacherRepository teacherRepositoryMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private TeacherDTO teacherDto;
    private Teacher teacher;
    private List<Teacher> teachers;
    private User user;
    
    @BeforeEach
    void setUp() {
        teacherDto = new TeacherDTO();
        teacher = new Teacher();
        teachers = Arrays.asList(teacher);
        user = UserMother.complete().build();
    }
    
    @Test
    void sortByLastName_ShouldSortSet() {
        UserDTO firstUser = UserDTO.builder().person(
                UserPersonDTO.builder().lastName(FIRST_TEACHER_LAST_NAME).build()).build();
        UserDTO secondUser = UserDTO.builder().person(
                UserPersonDTO.builder().lastName(SECOND_TEACHER_LAST_NAME).build()).build();
        TeacherDTO firstTeacher = TeacherDTO.builder().user(firstUser).build();
        TeacherDTO secondTeacher = TeacherDTO.builder().user(secondUser).build();
        List<TeacherDTO> teachers = Arrays.asList(secondTeacher, firstTeacher);
        Set<TeacherDTO> teachersSet = new HashSet<>(teachers);
        
        teacherService.sortByLastName(teachersSet);
        
        assertEquals(firstTeacher.getUser().getPerson().getLastName(), 
                     teachersSet.iterator().next().getUser().getPerson().getLastName());
    }
    
    @Test
    void sortByLastName_ShouldSortList() {
        UserDTO firstUser = UserDTO.builder().person(
                UserPersonDTO.builder().lastName(FIRST_TEACHER_LAST_NAME).build()).build();
        UserDTO secondUser = UserDTO.builder().person(
                UserPersonDTO.builder().lastName(SECOND_TEACHER_LAST_NAME).build()).build();
        TeacherDTO firstTeacher = TeacherDTO.builder().user(firstUser).build();
        TeacherDTO secondTeacher = TeacherDTO.builder().user(secondUser).build();
        List<TeacherDTO> teachers = Arrays.asList(secondTeacher, firstTeacher);

        teacherService.sortByLastName(teachers);
        
        assertEquals(firstTeacher.getUser().getPerson().getLastName(), 
                     teachers.iterator().next().getUser().getPerson().getLastName());
    }
    
    @Test
    void getTeacherByEmail_ShouldReturnTeacher() {
        when(teacherRepositoryMock.findByUserEmail(anyString())).thenReturn(teacher);
        teacherService.getTeacherByEmail(user.getEmail());
        verify(modelMapperMock).map(teacher, TeacherDTO.class);
    }
    
    @Test
    void getByUserId_ShouldReturnTeacher() {
        when(teacherRepositoryMock.findByUserId(anyInt())).thenReturn(teacher);
        teacherService.getByUserId(TEACHER_ID);
        verify(modelMapperMock).map(teacher, TeacherDTO.class);
    }
    
    @Test
    void getAll_ShouldReturnAllTeachers() {
        when(teacherRepositoryMock.findAll()).thenReturn(teachers);
        teacherService.getAll();
        verify(modelMapperMock).map(teachers, TEACHER_DTO_LIST_TYPE);
    }
    
    @Test
    void getByIdWithCourses_ShouldReturnTeacherWithOwnedCourses() {
        when(teacherRepositoryMock.findCoursesById(anyInt())).thenReturn(teacher);
        teacherService.getByIdWithCourses(TEACHER_ID);
        verify(modelMapperMock).map(teacher, TeacherDTO.class);
    }
    
    @Test
    void deleteById_ShouldDeleteTeacher() {
        teacherService.deleteById(TEACHER_ID);
        verify(teacherRepositoryMock).deleteById(anyInt());
    }
    
    @Test
    void create_ShouldCreateTeacher() {
        when(modelMapperMock.map(teacherDto, Teacher.class)).thenReturn(teacher);
        when(teacherRepositoryMock.saveAndFlush(isA(Teacher.class))).thenReturn(teacher);
        teacherService.create(teacherDto);
        verify(modelMapperMock).map(teacher, TeacherDTO.class);
    }
    
    @Test
    void update_ShouldUpdateTeacher() {
        teacherDto.setId(TEACHER_ID);
        when(teacherRepositoryMock.findById(anyInt())).thenReturn(teacher);
        when(modelMapperMock.map(teacherDto, Teacher.class)).thenReturn(teacher);
        teacherService.update(teacherDto);
        
        verify(modelMapperMock).map(teacherDto, Teacher.class);
    }
    
    @Test
    void getById_ShouldReturnTeacher() {
        when(teacherRepositoryMock.findById(anyInt())).thenReturn(teacher);
        teacherService.getById(TEACHER_ID);
        
        verify(modelMapperMock).map(teacher, TeacherDTO.class);
    }

    @Test
    void getByCoursesId_ShouldReturnTeachersForIdemCourse() {
        when(teacherRepositoryMock.findByCoursesId(anyInt())).thenReturn(teachers);
        teacherService.getByCoursesId(COURSE_ID);
        
        verify(modelMapperMock).map(teachers, TeacherServiceImpl.TEACHER_DTO_LIST_TYPE);
    }
}
