package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/stats")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class StatsController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);

    @Autowired
    public StatsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String statsPage(Model model) {
        logger.info("Entering statsPage method");

        List<User> admins = userService.findUsersByRole("ROLE_ADMIN");
        List<User> users = userService.findUsersByRole("ROLE_USER");

        model.addAttribute("adminCount", admins.size());
        model.addAttribute("userCount", users.size());

        // Count users by gender --------------------------------------------------------------
        long maleCount = userService.countByGender("male");
        long femaleCount = userService.countByGender("female");
        long nonBinaryCount = userService.countByGender("non_binary");
        long otherCount = userService.countByGender("other");
        long preferNotToSayCount = userService.countByGender("prefer_not_to_say");

        logger.info("Male Count: {}", maleCount);
        logger.info("Female Count: {}", femaleCount);
        logger.info("Non-binary Count: {}", nonBinaryCount);
        logger.info("Other Count: {}", otherCount);
        logger.info("Prefer not to say Count: {}", preferNotToSayCount);

        model.addAttribute("adminCount", admins.size());
        model.addAttribute("userCount", users.size());
        model.addAttribute("maleCount", maleCount);
        model.addAttribute("femaleCount", femaleCount);
        model.addAttribute("nonBinaryCount", nonBinaryCount);
        model.addAttribute("otherCount", otherCount);
        model.addAttribute("preferNotToSayCount", preferNotToSayCount);

        logger.info("Exiting statsPage method");
        return "stats/stats-main";
    }
}
