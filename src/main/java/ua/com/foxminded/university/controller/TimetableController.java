package ua.com.foxminded.university.controller;

import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.TimetableDTO;
import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.service.TimetableService;
import ua.com.foxminded.university.service.TimingService;

@Controller
@RequestMapping("/timetables")
@RequiredArgsConstructor
@Validated
public class TimetableController extends DefaultController {
    
    public static final String TIMING_ATTRIBUTE = "timing";
    public static final String TIMETABLE_ID_PARAMETER_NAME = "timetableId";
    public static final String TIMETABLE_ATTRIBUTE = "timetable";
    public static final int STUB = 0;
    public static final String TIMETABLES_ATTRIBUTE = "timetables"; 
    public static final String TIMETABLES_LIST_TEMPLATE_PATH = "timetables/list"; 
    
    private final TimetableService timetableService;
    private final TimingService timingService;
    
    @PostMapping("/delete-timing/{timetableId}/{timingId}")
    public String deleteTiming(@PathVariable int timetableId, 
                               @PathVariable int timingId) {
        timingService.deleteById(timingId);

        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TIMETABLES_LIST_TEMPLATE_PATH)
                                  .append(QUESTION_MARK)
                                  .append(TIMETABLE_ID_PARAMETER_NAME)
                                  .append(EQUAL_SIGN)
                                  .append(timetableId).toString();
    }
    
    @PostMapping("/delete/{timetableId}")
    public String delete(@PathVariable int timetableId) {
        timetableService.deleteById(timetableId);

        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TIMETABLES_LIST_TEMPLATE_PATH)
                                  .append(QUESTION_MARK)
                                  .append(TIMETABLE_ID_PARAMETER_NAME)
                                  .append(EQUAL_SIGN)
                                  .append(STUB).toString();
    }
    
    @PostMapping("/add-timing/{timetableId}")
    public String addTiming(@PathVariable int timetableId,                            
                            @Valid @ModelAttribute TimingDTO timing) {
        
        timing.setTimetable(TimetableDTO.builder().id(timetableId).build());
        timingService.create(timing);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TIMETABLES_LIST_TEMPLATE_PATH)
                                  .append(QUESTION_MARK)
                                  .append(TIMETABLE_ID_PARAMETER_NAME)
                                  .append(EQUAL_SIGN)
                                  .append(timetableId)
                                  .toString();
    }
    
    @PostMapping("/update-name/{timetableId}")
    public String updateName(@PathVariable int timetableId,
                             @ModelAttribute @Valid TimetableDTO timetable) {
        
        TimetableDTO persistedTimetable = timetableService.getById(timetableId);
        persistedTimetable.setName(timetable.getName());
        timetableService.update(persistedTimetable);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TIMETABLES_LIST_TEMPLATE_PATH)
                                  .append(QUESTION_MARK)
                                  .append(TIMETABLE_ID_PARAMETER_NAME)
                                  .append(EQUAL_SIGN)
                                  .append(timetableId).toString();
    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute @Valid TimetableDTO timetable) {
        
        TimetableDTO createdTimetable = timetableService.create(timetable);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TIMETABLES_LIST_TEMPLATE_PATH)
                                  .append(QUESTION_MARK)
                                  .append(TIMETABLE_ID_PARAMETER_NAME)
                                  .append(createdTimetable.getId()).toString();
    }
    
    @GetMapping("/list")
    public String getAll(@RequestParam int timetableId, Model model) {
        
        List<TimetableDTO> timetables = timetableService.getAll();
        timetableService.sortByName(timetables);
        
        TimetableDTO timetable;
        
        if (timetableId == STUB) {
            if (timetables.isEmpty()) {
                timetable = TimetableDTO.builder().timings(new HashSet<>()).build();
            } else {
                timetable = timetables.iterator().next();
            }
        } else {
            timetable = timetableService.getByIdWithTimings(timetableId);
        }
        
        timetableService.sortTimingsByStartTime(timetable);
        TimingDTO timing = new TimingDTO();
        
        model.addAttribute(TIMING_ATTRIBUTE, timing);
        model.addAttribute(TIMETABLE_ATTRIBUTE, timetable);
        model.addAttribute(TIMETABLES_ATTRIBUTE, timetables);
        return TIMETABLES_LIST_TEMPLATE_PATH;
    }
}
