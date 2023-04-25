package ua.com.foxminded.university.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.service.TimetableService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timetables")
public class TimetableController extends DefaultController {
    
    public static final String TIMETABLE_MODEL = "timetableModel";
    public static final String DAY_TIMETABLE = "dayTimetable";
    public static final String MONTH_TIMETABLE = "monthTimetable";
    
    private final TimetableService timetableService;
    
    @GetMapping("/monthtimetables/{datestamp}")
    public String getMonthTimetable(@PathVariable String datestamp, 
                                     Model model) throws ServiceException{
        LocalDate date = LocalDate.parse(datestamp);
        List<List<List<TimetableModel>>> monthTimetables = timetableService
                .getMonthTimetables(date);
        TimetableModel timetableModel = new TimetableModel();
        timetableModel.setDatestamp(date);
        model.addAttribute(MONTH_TIMETABLE, monthTimetables);
        model.addAttribute(TIMETABLE_MODEL, timetableModel);
        return "timetables/list";
    }
    
    @GetMapping("/daytimetables/{datestamp}")
    public String getDayTimetable(@PathVariable String datestamp, 
                                   Model model) throws ServiceException {
        LocalDate localDate = LocalDate.parse(datestamp);
        List<TimetableModel> dayTimetable = timetableService.getDayTimetalbes(localDate);
        TimetableModel timetableModel = new TimetableModel();
        timetableModel.setDatestamp(localDate);
        timetableModel.setCourse(new CourseModel());
        timetableModel.getCourse().setTeachers(new HashSet<>());
        timetableModel.getCourse().getTeachers().add(new TeacherModel());
        model.addAttribute(DAY_TIMETABLE, dayTimetable);
        model.addAttribute(TIMETABLE_MODEL, timetableModel);
        return "timetables/day-timetables";
    }
    
    @PostMapping(value = "/back")
    public String back(@ModelAttribute TimetableModel timetableModel, 
                       Model model) throws ServiceException {
        
        LocalDate previousMothDatestamp = timetableModel.getDatestamp()
                                                        .minusWeeks(3);
        List<List<List<TimetableModel>>> monthTimetable = timetableService
                .getMonthTimetables(previousMothDatestamp);
        timetableModel.setDatestamp(previousMothDatestamp);
        model.addAttribute(MONTH_TIMETABLE, monthTimetable);
        model.addAttribute(timetableModel);
        return "timetables/list";
    }
    
    @PostMapping(value = "/next")
    public String next(@ModelAttribute TimetableModel timetableModel, 
                       Model model) throws ServiceException {
        
        LocalDate nextMonthDatestamp = timetableModel.getDatestamp().plusWeeks(3);
        List<List<List<TimetableModel>>> monthTimtables = timetableService
                .getMonthTimetables(nextMonthDatestamp);
        timetableModel.setDatestamp(nextMonthDatestamp);
        model.addAttribute(MONTH_TIMETABLE, monthTimtables);
        model.addAttribute(timetableModel);
        return "timetables/list";
    }
    
    @GetMapping(value = "/list")
    public String list(Model model) throws ServiceException {
        LocalDate currentDate = LocalDate.now();
        List<List<List<TimetableModel>>> monthTimetable = timetableService
                .getMonthTimetables(currentDate);
        TimetableModel timetableModel = new TimetableModel();
        timetableModel.setDatestamp(currentDate);
        model.addAttribute(MONTH_TIMETABLE, monthTimetable);
        model.addAttribute(TIMETABLE_MODEL, timetableModel);
        return "timetables/list";
    }
}
