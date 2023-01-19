package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.service.TimetableService;

@Slf4j
@Controller
public class TimetableController {
    
    private TimetableService<TimetableModel> timetableService;
    
    @Autowired
    public TimetableController(TimetableService<TimetableModel> timetableService) {
        this.timetableService = timetableService;
    }
    
    @RequestMapping("/timetables/list")
    public String getAllTimetables(Model model) {
        try {
            List<TimetableModel> timetables = timetableService.getAllTimetables();
            model.addAttribute("timetables", timetables);
        } catch (ServiceException e) {
            log.error("Getting all timetables was failed", e);
        }
        return "timetables/list";
    }
}
