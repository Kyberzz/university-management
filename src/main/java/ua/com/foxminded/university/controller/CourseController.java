package ua.com.foxminded.university.controller;

import java.util.List;

import javax.validation.Valid;

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
    
    private final CourseService courseService;
    
    @GetMapping("/{id}")
    public String get(@PathVariable int id, Model model) throws ServiceException {
        CourseModel course = courseService.getTimetableAndTeachersByCourseId(id);
        CourseModel updatedCourse = new CourseModel();
        model.addAttribute("updatedCourse", updatedCourse);
        model.addAttribute("course", course);
        return "courses/course";
    }
    
    @PostMapping(value = "/delete", params = "courseId")
    public String delete(@RequestParam Integer courseId) throws ServiceException {
        courseService.deleteById(courseId);
        return "redirect:/courses/list";
    }
    
    @PostMapping(value = "/update", params = "courseId")
    public String update(@RequestParam Integer courseId,
                         @Valid @ModelAttribute CourseModel updatedCourse, 
                         BindingResult bindingResult) throws BindException, 
                                                             ServiceException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        CourseModel persistedCourse = courseService.getById(courseId);
        persistedCourse.setDescription(updatedCourse.getDescription());
        persistedCourse.setName(updatedCourse.getName());
        courseService.update(persistedCourse);
        return "redirect:/courses/" + courseId;
    }
    
    @PostMapping(value = "/create")
    public String create(@Valid @ModelAttribute CourseModel course, 
                         BindingResult bindingResult) throws BindException, 
    ServiceException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        courseService.create(course);   
        return "redirect:/courses/list";
    }
    
    @GetMapping("/list")
    public String list(Model model) throws ServiceException {
        CourseModel course = new CourseModel();
        List<CourseModel> courses = courseService.getAll();
        model.addAttribute("courses", courses);
        model.addAttribute("course", course);
        return "courses/list";
    }
}
