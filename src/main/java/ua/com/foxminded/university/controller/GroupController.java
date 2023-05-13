package ua.com.foxminded.university.controller;

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
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController extends DefaultController {
    
    public static final String STUDENTS_MODEL_ATTRIBUTE = "students";
    public static final String GROUP_MODEL_ATTRIBUTE = "groupModel";
    public static final String GROUPS_PATH = "/groups/";

    private final GroupService groupService;
    private final StudentService studentService;
    
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
    public String delete(@PathVariable int groupId) throws ServiceException {
        groupService.deleteById(groupId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(GROUPS_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }

    @PostMapping(value = "/create", params = "name")
    public String create(@RequestParam String name) throws ServiceException {
        GroupModel group = GroupModel.builder().name(name).build();
        groupService.create(group);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(GROUPS_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }

    @PostMapping(value = "/{groupId}/update", params = "name")
    public String update(@PathVariable int groupId, @RequestParam String name) 
            throws ServiceException {
        GroupModel group = GroupModel.builder().id(groupId).name(name).build();
        groupService.update(group);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(GROUPS_PATH)
                                  .append(groupId).toString();
    }

    @GetMapping("/{groupId}")
    public String getById(@PathVariable int groupId, Model model) throws ServiceException {
        GroupModel groupModel = groupService.getGroupRelationsById(groupId);
        groupService.sortStudentsByLastName(groupModel);
        List<StudentModel> students = studentService.getAll();
        studentService.sortByLastName(students);
        model.addAttribute(STUDENTS_MODEL_ATTRIBUTE, students);
        model.addAttribute(GROUP_MODEL_ATTRIBUTE, groupModel);
        return "groups/group";
    }

    @GetMapping("/list")
    public String getAllGroups(Model model) throws ServiceException {
        List<GroupModel> groups = groupService.getAll();
        Collections.sort(groups, Comparator.comparing(GroupModel::getName));
        model.addAttribute("groups", groups);
        return "groups/list";
    }
}
