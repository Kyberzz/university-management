package ua.com.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.controller.DefaultControllerTest.ADMIN_EMAIL;
import static ua.com.foxminded.university.controller.DefaultControllerTest.TEACHER_EMAIL;
import static ua.com.foxminded.university.controller.TimetableController.STUB;
import static ua.com.foxminded.university.controller.TimetableController.TIMETABLE_ATTRIBUTE;
import static ua.com.foxminded.university.controller.TimetableController.TIMING_ATTRIBUTE;
import static ua.com.foxminded.university.controller.TimetableControllerTest.TIMETABLE_ID;
import static ua.com.foxminded.university.controller.TimetableControllerTest.TIMETABLE_NAME;
import static ua.com.foxminded.university.entity.Authority.ADMIN;
import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_ADMIN;
import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_TEACHER;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dto.TimetableDTO;
import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.dtomother.TimingDTOMother;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entity.UserAuthority;
import ua.com.foxminded.university.entitymother.TimingMother;
import ua.com.foxminded.university.entitymother.UserMother;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.repository.TimingRepository;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class TimetableControllerIntegrationTest {
    
    private static final String TIMETABLE_NEW_NAME = "short lessons";
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    @Autowired
    private TimingRepository timingRepository;
    
    private User adminUser;
    private User teacherUser;
    private UserAuthority adminUserAuthority;
    private UserAuthority teacherUserAuthority;
    private Timetable timetable;
    private TimetableDTO timetableDto;
    private TimingDTO timingDto;
    private Timing timing;
    
    @BeforeTransaction
    void init() {
        adminUser = UserMother.complete().email(ADMIN_EMAIL).build();
        userRepository.saveAndFlush(adminUser);
        teacherUser = UserMother.complete().email(TEACHER_EMAIL).build();
        userRepository.saveAndFlush(teacherUser);
        
        adminUserAuthority = UserAuthority.builder()
                                          .roleAuthority(ROLE_ADMIN)
                                          .user(adminUser).build();
        userAuthorityRepository.saveAndFlush(adminUserAuthority);
        teacherUserAuthority = UserAuthority.builder()
                                            .roleAuthority(ROLE_TEACHER)
                                            .user(teacherUser).build();
        userAuthorityRepository.saveAndFlush(teacherUserAuthority);
        
        timetable = Timetable.builder().name(TIMETABLE_NAME).build();
        timetableRepository.saveAndFlush(timetable);
        timetableDto = TimetableDTO.builder().name(TIMETABLE_NEW_NAME).build();
        
        timingDto = TimingDTOMother.complete().build();
        
        timing = TimingMother.complete().timetable(timetable).build();
        timingRepository.saveAndFlush(timing);
    }
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void deleteTiming_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/timetables/delete-timing/{timetableId}/{timingId}", 
                             timetable.getId(), 
                             timing.getId())
                    .with(csrf()))
               .andDo(print())
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete_ShouldReturnStatusIsOk() throws Exception {
        mockMvc.perform(post("/timetables/delete/{timetableId}", timetable.getId())
                    .with(csrf()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void addTiming_ShouldReturnStatusIsOk() throws Exception {
        LocalTime startTime = LocalTime.of(12, 0);
        timingDto.setStartTime(startTime);;
        
        mockMvc.perform(post("/timetables/add-timing/{timetableId}", timetable.getId())
                    .with(csrf())
                    .flashAttr(TIMING_ATTRIBUTE, timingDto))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void updateName_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/timetables/update-name/{timetableId}", timetable.getId())
                    .flashAttr(TIMETABLE_ATTRIBUTE, timetableDto)
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void create_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/timetables/create")
                    .flashAttr(TIMETABLE_ATTRIBUTE, timetableDto)
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAll_ShouldReturnStatusIsOk_TimetableIdIsNotNull() throws Exception {
        mockMvc.perform(get("/timetables/list")
                    .param("timetableId", String.valueOf(TIMETABLE_ID)))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAll_ShouldReturnStatusIsOk_WhenFirstIfBlockIsTrueAndSecondOneIsFalse() 
            throws Exception {
        mockMvc.perform(get("/timetables/list")
                    .param("timetableId", String.valueOf(STUB)))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAll_ShouldReturnStatusIsOk_WhenFirstIfBlockIsTrueAndSecondOneIsTrue() 
            throws Exception {
        mockMvc.perform(get("/timetables/list")
                    .param("timetableId", String.valueOf(STUB)))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().isOk());
    }
}
