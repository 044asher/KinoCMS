package com.CMS.kinoCMS.controllers.Admin;

import com.CMS.kinoCMS.models.News;
import com.CMS.kinoCMS.services.NewsService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/admin/news")
public class NewsController {
    private static final Logger logger = LogManager.getLogger(NewsController.class);

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public String news(Model model) {
        model.addAttribute("news", newsService.findAll());
        return "/news/news-list";
    }

    @GetMapping("/{id}")
    public String newsEdit(@PathVariable long id, Model model) {
        News news = newsService.findById(id).orElseThrow(() -> new IllegalArgumentException("News not found"));
        model.addAttribute("news", news);
        return "/news/news-edit";
    }

    @PostMapping("/{id}")
    public String newsUpdate(@PathVariable long id, @Valid News news, BindingResult bindingResult,
                             @RequestParam(required = false) MultipartFile file,
                             @RequestParam(required = false, value = "additionalFiles") MultipartFile[] additionalFiles) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors in newsUpdate: {}", bindingResult.getAllErrors());
            return "/news/news-edit";
        }
        try {
            newsService.updateNews(id, news, file, additionalFiles);
        } catch (IOException e) {
            logger.error("Error updating news with ID {}", id, e);
        }
        return "redirect:/admin/news";
    }

    @GetMapping("/add")
    public String newsAdd(Model model) {
        model.addAttribute("news", new News());
        return "/news/news-add";
    }

    @PostMapping("/add")
    public String newsAdd(@Valid News news, BindingResult bindingResult,
                          @RequestParam(required = false) MultipartFile file,
                          @RequestParam(value = "additionalFiles") MultipartFile[] additionalFiles) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors in newsAdd: {}", bindingResult.getAllErrors());
            return "/news/news-add";
        }
        try {
            newsService.addNews(news, file, additionalFiles);
        } catch (IOException e) {
            logger.error("Error adding news: {}", news.getName(), e);
        }
        return "redirect:/admin/news";
    }

    @PostMapping("/delete/{id}")
    public String newsDelete(@PathVariable long id) {
        newsService.delete(newsService.findById(id).orElseThrow(() -> new IllegalArgumentException("News not found")));
        return "redirect:/admin/news";
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<Void> changeStatus(@PathVariable long id) {
        Optional<News> optionalNews = newsService.findById(id);
        if (optionalNews.isPresent()) {
            News news = optionalNews.get();
            news.setNotActive(!news.isNotActive());
            newsService.save(news);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("News with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
