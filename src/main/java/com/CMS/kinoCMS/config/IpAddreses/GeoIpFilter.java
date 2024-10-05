package com.CMS.kinoCMS.config.IpAddreses;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class GeoIpFilter extends OncePerRequestFilter {
    // Тут мы в каждом HTTP запросе получаем IP пользователя для определения его
    // местоположения и сохранить эту информацию в сессию


    @Autowired
    private GeoIPService geoIPService;

    @Getter
    private final Map<String, String> userLocations = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String ip = request.getRemoteAddr();

        String uri = request.getRequestURI();
        if (uri.startsWith("/static/") || uri.startsWith("/resources/") || uri.startsWith("/assets/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (session.getAttribute("location") == null) {
            if (!"0:0:0:0:0:0:0:1".equals(ip) && !"127.0.0.1".equals(ip)) {
                log.info("IP: {}", ip);
                String location = geoIPService.getCityAndCountry(ip);
                session.setAttribute("location", location);
            } else {
                log.info("IP: {}", ip);
                ip = "169.150.218.78";
                String location = geoIPService.getCityAndCountry(ip);
                session.setAttribute("location", location);
            }
        }

        String sessionId = session.getId();
        String location = (String) session.getAttribute("location");
        userLocations.put(sessionId, location);

        filterChain.doFilter(request, response);
    }

}

