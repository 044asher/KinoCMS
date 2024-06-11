package com.CMS.kinoCMS.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
public class HttpErrorController implements ErrorController {
    private final MessageSource messageSource;

    @Autowired
    public HttpErrorController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Locale locale, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                String errorMessage = messageSource.getMessage("error.404.message", null, locale);
                model.addAttribute("errorCode", "404");
                model.addAttribute("errorMessage", errorMessage);
                return "errors/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                String errorMessage = messageSource.getMessage("error.500.message", null, locale);
                model.addAttribute("errorCode", "500");
                model.addAttribute("errorMessage", errorMessage);
                return "errors/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                String errorMessage = messageSource.getMessage("error.403.message", null, locale);
                model.addAttribute("errorCode", "403");
                model.addAttribute("errorMessage", errorMessage);
                return "errors/403";
            }
        }
        return "errors/http-errors";
    }
}
