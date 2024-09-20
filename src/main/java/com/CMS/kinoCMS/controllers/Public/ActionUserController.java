package com.CMS.kinoCMS.controllers.Public;

import com.CMS.kinoCMS.models.Action;
import com.CMS.kinoCMS.services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/actions")
public class ActionUserController {
    private final ActionService actionService;

    @Autowired
    public ActionUserController(ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping
    public String actions(Model model) {
        List<Action> actions = actionService.findByNotActive(false);
        model.addAttribute("actions", actions);
        return "users-part/actions/actions";
    }

    @GetMapping("/{id}")
    public String action(Model model, @PathVariable long id) {
        Optional<Action> action = actionService.findById(id);
        action.ifPresent(value -> model.addAttribute("action", value));
        return "users-part/actions/action-details";
    }
}
