package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.service.TimetableService;

@Controller
public class TimetableController extends DefaultController {

    private TimetableService<TimetableModel> timetableService;

    public TimetableController(TimetableService<TimetableModel> timetableService) {
        this.timetableService = timetableService;
    }

    @RequestMapping("/timetables/list")
    public String getAllTimetables(Model model) throws ServiceException {
        List<TimetableModel> timetables = timetableService.getAllTimetables();
        model.addAttribute("timetables", timetables);
        return "timetables/list";
    }
}
