package com.CMS.kinoCMS.config.ControllerAdvices;

import com.CMS.kinoCMS.config.MyUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof MyUserDetails) {
                MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
                request.setAttribute("firstName", userDetails.getFirstName());
                request.setAttribute("lastName", userDetails.getLastName());
                request.setAttribute("role", userDetails.getRole());
            }
        }
        return true;
    }
}
