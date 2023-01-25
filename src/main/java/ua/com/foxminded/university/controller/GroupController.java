package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.service.GroupService;

@Slf4j
@Controller
public class GroupController extends DefaultController {
    
    private GroupService<GroupModel> groupService;

    @Autowired
    public GroupController(GroupService<GroupModel> groupService) {
        this.groupService = groupService;
    }
    
    @RequestMapping("/groups/list")
    public String getAllGroups(Model model) {
        try {
            List<GroupModel> groups = groupService.getAllGroups();
            model.addAttribute("groups", groups);
        } catch (ServiceException e) {
            log.error("Getting all groups was failed", e);
        }
        return "groups/list";
    }
}
