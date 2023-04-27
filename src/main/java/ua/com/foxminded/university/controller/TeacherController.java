package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.service.TeacherService;

@Controller
@RequiredArgsConstructor
public class TeacherController extends DefaultController {

    private final TeacherService teacherService;

    @RequestMapping(value = "/teachers/list")
    public String getAllTeachers(Model model) throws ServiceException {
        List<TeacherModel> teachers = teacherService.getAll();
        model.addAttribute("teachers", teachers);
        return "teachers/list";
    }
}
