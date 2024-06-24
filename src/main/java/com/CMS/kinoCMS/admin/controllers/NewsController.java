package com.CMS.kinoCMS.admin.controllers;

import com.CMS.kinoCMS.admin.services.NewsService;
import com.CMS.kinoCMS.admin.models.News;
import com.CMS.kinoCMS.admin.services.FileUploadService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/news")
public class NewsController {
    private static final Logger logger = LogManager.getLogger(NewsController.class);

    private final NewsService newsService;
    private final FileUploadService fileUploadService;

    @Autowired
    public NewsController(NewsService newsService, FileUploadService fileUploadService) {
        this.newsService = newsService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public String news(Model model) {
        logger.info("Fetching all news");
        model.addAttribute("news", newsService.findAll());
        return "/news/news-list";
    }

    @GetMapping("/{id}")
    public String newsEdit(@PathVariable long id, Model model) {
        logger.info("Fetching news with ID {}", id);
        News news = newsService.findById(id).orElseThrow(() -> new IllegalArgumentException("News not found"));
        model.addAttribute("news", news);
        return "/news/news-edit";
    }

    @PostMapping("/{id}")
    public String newsUpdate(@PathVariable long id, @Valid News news, BindingResult bindingResult,
                             @RequestParam(required = false) MultipartFile file,
                             @RequestParam(required = false, value = "additionalFiles") MultipartFile[] additionalFiles) {
        logger.info("Updating news with ID {}", id);
        if(bindingResult.hasErrors()) {
            logger.warn("Validation errors in newsUpdate: {}", bindingResult.getAllErrors());
            return "/news/news-edit";
        }
        try{
            News existingNews = newsService.findById(id).orElseThrow(() -> new IllegalArgumentException("News not found"));

            if(file != null && !file.isEmpty()) {
                String resultMainImage = fileUploadService.uploadFile(file);
                existingNews.setMainImage(resultMainImage);
                logger.info("Uploaded file for news with ID {}: {}", id, resultMainImage);
            }

            if(additionalFiles != null && additionalFiles.length > 0) {
                List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
                existingNews.getImages().clear();
                existingNews.getImages().addAll(newImageNames.stream().limit(5).toList());
                logger.info("Uploaded additional files for news with id: {}", id);
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
            logger.info("News with ID {} updated successfully", id);
        } catch (IOException e){
            logger.error("Error updating news with ID " + id, e);
            e.printStackTrace();
        }
        return "redirect:/admin/news";
    }

    @GetMapping("/add")
    public String newsAdd(Model model){
        logger.info("Preparing to add news");
        model.addAttribute("news", new News());
        return "/news/news-add";
    }

    @PostMapping("/add")
    public String newsAdd(@Valid News news, BindingResult bindingResult,
                          @RequestParam(required = false) MultipartFile file,
                          @RequestParam(value = "additionalFiles") MultipartFile[] additionalFiles){
        logger.info("Adding new news: {}", news.getName());
        if(bindingResult.hasErrors()) {
            logger.warn("Validation errors in newsAdd: {}", bindingResult.getAllErrors());
            return "/news/news-add";
        }
        try{
            if(file != null && !file.isEmpty()) {
                String resultMainImage = fileUploadService.uploadFile(file);
                news.setMainImage(resultMainImage);
                logger.info("Uploaded file for new news: {}", resultMainImage);
            }

            if (additionalFiles != null && additionalFiles.length > 0) {
                List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
                news.getImages().addAll(newImageNames.stream().limit(5).toList());
                logger.info("Uploaded additional files for new news");
            }
            news.setDateOfCreation(LocalDate.now());
            newsService.save(news);
            logger.info("New news added successfully");
        }catch (IOException e){
            logger.error("Error adding news: " + news.getName(), e);
            e.printStackTrace();
        }
        return "redirect:/admin/news";
    }

    @PostMapping("/delete/{id}")
    public String newsDelete(@PathVariable long id) {
        logger.info("Deleting news with ID {}", id);
        newsService.delete(newsService.findById(id).orElseThrow(() -> new IllegalArgumentException("News not found")));
        return "redirect:/admin/news";
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<Void> changeStatus(@PathVariable long id){
        logger.info("Changing status for news with ID {}", id);
        Optional<News> optionalNews = newsService.findById(id);
        if(optionalNews.isPresent()) {
            News news = optionalNews.get();
            news.setNotActive(!news.isNotActive());
            newsService.save(news);
            logger.info("Status changed successfully for news with ID {}", id);
            return ResponseEntity.ok().build();
        }else {
            logger.warn("News with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
