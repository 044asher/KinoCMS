package com.CMS.kinoCMS.controllers.Admin;

import com.CMS.kinoCMS.config.IpAddreses.GeoIpFilter;
import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/stats")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Log4j2
public class StatsController {

    private final UserService userService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private GeoIpFilter geoIpFilter;

    @Autowired
    public StatsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String statsPage(Model model) {
        List<User> admins = userService.findUsersByRole("ROLE_ADMIN");
        List<User> users = userService.findUsersByRole("ROLE_USER");

        model.addAttribute("adminCount", admins.size());
        model.addAttribute("userCount", users.size());

        // Count users by gender
        long maleCount = userService.countByGender("male");
        long femaleCount = userService.countByGender("female");
        long nonBinaryCount = userService.countByGender("non_binary");
        long otherCount = userService.countByGender("other");
        long preferNotToSayCount = userService.countByGender("prefer_not_to_say");

        model.addAttribute("maleCount", maleCount);
        model.addAttribute("femaleCount", femaleCount);
        model.addAttribute("nonBinaryCount", nonBinaryCount);
        model.addAttribute("otherCount", otherCount);
        model.addAttribute("preferNotToSayCount", preferNotToSayCount);

        Map<String, String> activeUsersWithLocations = getActiveUsersWithLocations();
        Map<String, String> activeAdminsWithLocations = new HashMap<>();
        Map<String, String> activeUsersWithLocationsMap = new HashMap<>();

        for (Map.Entry<String, String> entry : activeUsersWithLocations.entrySet()) {
            String username = entry.getKey();
            String location = entry.getValue();
            User user = userService.findByUsername(username).get();
            if (user.getRole().contains("ROLE_ADMIN")) {
                activeAdminsWithLocations.put(username, location);
            } else if (user.getRole().contains("ROLE_USER")) {
                activeUsersWithLocationsMap.put(username, location);
            }
        }

        model.addAttribute("activeAdminsWithLocations", activeAdminsWithLocations);
        model.addAttribute("activeUsersWithLocations", activeUsersWithLocationsMap);
        model.addAttribute("activeUserCount", activeUsersWithLocations.size());

        return "stats/stats-main";
    }


    public Map<String, String> getActiveUsersWithLocations() {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        Map<String, String> userLocations = new HashMap<>();

        log.info("Fetching active users. Total principals: {}", principals.size());

        for (Object principal : principals) {
            if (principal instanceof UserDetails userDetails) {
                List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);
                log.info("User: {}, Sessions count: {}", userDetails.getUsername(), sessions.size());

                if (!sessions.isEmpty()) {
                    SessionInformation sessionInformation = sessions.get(0);
                    String sessionId = sessionInformation.getSessionId();

                    // Получаем местоположение из массива
                    String location = geoIpFilter.getUserLocations().get(sessionId);
                    log.info("User: {}, Location: {}", userDetails.getUsername(), location);
                    userLocations.put(userDetails.getUsername(), location != null ? location : "Unknown Location");
                }
            }
        }
        log.info("Active principals: {}", principals.size());
        return userLocations;
    }
}
