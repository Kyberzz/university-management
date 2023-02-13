package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.service.GroupService;

@Controller
public class GroupController extends DefaultController {

    private GroupService<GroupModel> groupService;

    public GroupController(GroupService<GroupModel> groupService) {
        this.groupService = groupService;
    }

    @RequestMapping("/groups/list")
    public String getAllGroups(Model model) throws ServiceException {
        List<GroupModel> groups = groupService.getAllGroups();
        model.addAttribute("groups", groups);
        return "groups/list";
    }
}
