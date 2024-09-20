package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.models.News;
import com.CMS.kinoCMS.repositories.NewsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class NewsService {
    private final NewsRepository newsRepository;
    private final FileUploadService fileUploadService;

    @Autowired
    public NewsService(NewsRepository newsRepository, FileUploadService fileUploadService) {
        this.newsRepository = newsRepository;
        this.fileUploadService = fileUploadService;
    }

    public List<News> findAll() {
        log.info("Start NewsService - findAll");
        List<News> newsList = newsRepository.findAll();
        log.info("End NewsService - findAll. Retrieved {} news items", newsList.size());
        return newsList;
    }

    public List<News> findByNotActive(boolean notActive) {
        log.info("Start NewsService - findByNotActive with parameter notActive: {}", notActive);
        List<News> newsList = newsRepository.findByNotActive(notActive);
        log.info("End NewsService - findByNotActive. Retrieved {} news items", newsList.size());
        return newsList;
    }

    public Optional<News> findById(Long id) {
        log.info("Start NewsService - findById with id: {}", id);
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            log.info("End NewsService - findById. Found news with id: {}", id);
        } else {
            log.warn("End NewsService - findById. No news found with id: {}", id);
        }
        return news;
    }

    public void save(News news) {
        log.info("Start NewsService - save with news: {}", news);
        newsRepository.save(news);
        log.info("End NewsService - save. News saved: {}", news);
    }

    public void delete(News news) {
        log.info("Start NewsService - delete with news: {}", news);
        newsRepository.delete(news);
        log.info("End NewsService - delete. News deleted: {}", news);
    }

    public void addNews(News news, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        log.info("Start NewsService - addNews with news: {}", news);

        if (file != null && !file.isEmpty()) {
            String resultMainImage = fileUploadService.uploadFile(file);
            news.setMainImage(resultMainImage);
            log.info("Added main image: {}", resultMainImage);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            news.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Added additional images: {}", newImageNames);
        }

        news.setDateOfCreation(LocalDate.now());
        save(news);
        log.info("End NewsService - addNews. News added: {}", news);
    }

    public void updateNews(long id, News news, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        log.info("Start NewsService - updateNews with id: {}", id);

        News existingNews = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found with id: " + id));

        if (file != null && !file.isEmpty()) {
            String resultMainImage = fileUploadService.uploadFile(file);
            existingNews.setMainImage(resultMainImage);
            log.info("Updated main image: {}", resultMainImage);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            existingNews.getImages().clear();
            existingNews.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Updated additional images: {}", newImageNames);
        }

        existingNews.setName(news.getName());
        existingNews.setDescription(news.getDescription());
        existingNews.setDateOfCreation(Optional.ofNullable(news.getDateOfCreation()).orElse(existingNews.getDateOfCreation()));
        existingNews.setLink(news.getLink());
        existingNews.setUrlSEO(news.getUrlSEO());
        existingNews.setKeywordsSEO(news.getKeywordsSEO());
        existingNews.setDescriptionSEO(news.getDescriptionSEO());
        existingNews.setTitleSEO(news.getTitleSEO());

        save(existingNews);
        log.info("End NewsService - updateNews. News updated: {}", existingNews);
    }
}
