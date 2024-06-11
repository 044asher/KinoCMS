package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.News;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/admin/news")
public class NewsController {
    private final NewsService newsService;
    private final FileUploadService fileUploadService;

    @Autowired
    public NewsController(NewsService newsService, FileUploadService fileUploadService) {
        this.newsService = newsService;
        this.fileUploadService = fileUploadService;
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
                             @RequestParam(required = false) MultipartFile file) {
        if(bindingResult.hasErrors()) {
            return "/news/news-edit";
        }
        try{
            News existingNews = newsService.findById(id).orElseThrow(() -> new IllegalArgumentException("News not found"));

            if(file != null && !file.isEmpty()) {
                String resultMainImage = fileUploadService.uploadFile(file);
                existingNews.setMainImage(resultMainImage);
            }

            existingNews.setName(news.getName());
            existingNews.setDescription(news.getDescription());
            if(news.getDateOfCreation() != null) {
                existingNews.setDateOfCreation(news.getDateOfCreation());
            }
            existingNews.setLink(news.getLink());

            existingNews.setUrlSEO(news.getUrlSEO());
            existingNews.setKeywordsSEO(news.getKeywordsSEO());
            existingNews.setDescriptionSEO(news.getDescriptionSEO());
            existingNews.setTitleSEO(news.getTitleSEO());

            newsService.save(existingNews);
        } catch (IOException e){
            e.printStackTrace();
        }
        return "redirect:/admin/news";
    }

    @GetMapping("/add")
    public String newsAdd(Model model){
        model.addAttribute("news", new News());
        return "/news/news-add";
    }

    @PostMapping("/add")
    public String newsAdd(@Valid News news, BindingResult bindingResult,
                          @RequestParam(required = false) MultipartFile file){
        if(bindingResult.hasErrors()) {
            return "/news/news-add";
        }
        try{
            if(file != null && !file.isEmpty()) {
                String resultMainImage = fileUploadService.uploadFile(file);
                news.setMainImage(resultMainImage);
            }
            news.setDateOfCreation(LocalDate.now());
            newsService.save(news);
        }catch (IOException e){
            e.printStackTrace();
        }
        return "redirect:/admin/news";
    }

    @PostMapping("/delete/{id}")
    public String newsDelete(@PathVariable long id) {
        newsService.delete(newsService.findById(id).orElseThrow(() -> new IllegalArgumentException("News not found")));
        return "redirect:/admin/news";
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<Void> changeStatus(@PathVariable long id){
        Optional<News> optionalNews = newsService.findById(id);
        if(optionalNews.isPresent()) {
            News news = optionalNews.get();
            news.setNotActive(!news.isNotActive());
            newsService.save(news);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}







