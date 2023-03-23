package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.service.CourseService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController extends DefaultController {
    
    public static final String HAS_ROLE_ADMIN = "hasRole('ADMIN')";
    public static final String HAS_ROLE_STAFF_OR_ADMIN = "hasRole('STAFF', 'ADMIN')";

    private final CourseService<CourseModel> courseService;
    
    @GetMapping("/{id}")
    public String get(@PathVariable int id, 
                      @ModelAttribute CourseModel course) throws ServiceException {
        course = courseService.getById(id);
        return "courses/course";
    }
    
    @PostMapping(value = "/delete", params = "courseId")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public String delete(@RequestParam Integer courseId) throws ServiceException {
        courseService.deleteById(courseId);
        return "redirect:/courses/list";
    }
    
    @PostMapping(value = "/update", params = "courseId")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public String update(@RequestParam Integer courseId,
                         @ModelAttribute CourseModel course, 
                         BindingResult bindingResult) throws BindException, 
                                                             ServiceException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        CourseModel preservedCourse = courseService.getById(courseId);
        preservedCourse.setDescription(course.getDescription());
        preservedCourse.setName(course.getName());
        return "redirect:/courses/" + courseId;
    }
    
    @GetMapping("/list")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public String list(Model model) throws ServiceException {
        List<CourseModel> courses = courseService.getAll();
        model.addAttribute("courses", courses);
        return "courses/list";
    }
    
    @PostMapping("/create")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public String create(@ModelAttribute CourseModel course, 
                         BindingResult bindingResult) throws BindException, 
                                                             ServiceException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        courseService.create(course);   
        return "courses/list";
    }
}
