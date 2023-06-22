package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.LessonController.LESSONS_ATTRIBUTE;
import static ua.com.foxminded.university.controller.StudentController.STUDENTS_ATTRIBUTE;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.comparator.LessonDTOComparator;
import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.StudentService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController extends DefaultController {
    
    public static final int GROUP_ID = 1;
    public static final String GROUP_NAME_PARAMETER = "name";
    public static final String GROUP_ID_PARAMETER_NAME = "groupId";
    public static final String GROUPS_ATTRIBUTE = "groups";
    public static final String GROUP_ATTRIBUTE = "group";
    public static final String GROUPS_PATH = "/groups/";
    public static final String GROUPS_LIST_TEMPLATE_PATH = "groups/list";
    public static final String GROUP_TEMPLATE_PATH = "groups/group";

    private final GroupService groupService;
    private final StudentService studentService;
    private final LessonService lessonService;
    
    @PostMapping("/{groupId}/deassign-group")
    public String deassignGroup(@PathVariable int groupId, 
                                @RequestParam int studentId) {
        groupService.deassignGroup(studentId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(GROUPS_PATH)
                                  .append(groupId).toString();
    }
    
    @PostMapping("/{groupId}/assign-group")
    public String assignGroup(@PathVariable int groupId,
                              @RequestParam int[] studentId) {
        groupService.assignGroup(groupId, studentId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(GROUPS_PATH)
                                  .append(groupId).toString();
    }

    @PostMapping("/{groupId}/delete")
    public String delete(@PathVariable int groupId) {
        groupService.deleteById(groupId);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(GROUPS_LIST_TEMPLATE_PATH)
                                  .toString();
    }

    @PostMapping("/create")
    public String create(@RequestParam String name) {
        GroupDTO group = GroupDTO.builder().name(name).build();
        groupService.create(group);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(GROUPS_LIST_TEMPLATE_PATH)
                                  .toString();
    }

    @PostMapping("/{groupId}/update")
    public String update(@PathVariable int groupId, 
                         @RequestParam(GROUP_NAME_PARAMETER) String name) {
        GroupDTO group = GroupDTO.builder().id(groupId).name(name).build();
        groupService.update(group);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(GROUPS_PATH)
                                  .append(groupId).toString();
    }

    @GetMapping("/{groupId}")
    public String getById(@PathVariable int groupId, Model model) {
        GroupDTO group = groupService.getGroupRelationsById(groupId);
        groupService.sortContainedStudentsByLastName(group);
        List<StudentDTO> students = studentService.getAll();
        studentService.sortByLastName(students);
        List<LessonDTO> lessons = lessonService.getByGroupId(groupId);
        lessons.sort(new LessonDTOComparator());
        
        model.addAttribute(LESSONS_ATTRIBUTE, lessons);
        model.addAttribute(STUDENTS_ATTRIBUTE, students);
        model.addAttribute(GROUP_ATTRIBUTE, group);
        return GROUP_TEMPLATE_PATH;
    }

    @GetMapping("/list")
    public String getAll(Model model) {
        List<GroupDTO> groups = groupService.getAll();
        Collections.sort(groups, Comparator.comparing(GroupDTO::getName));
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        return GROUPS_LIST_TEMPLATE_PATH;
    }
}
