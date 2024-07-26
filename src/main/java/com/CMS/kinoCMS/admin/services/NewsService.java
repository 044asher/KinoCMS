package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.News;
import com.CMS.kinoCMS.admin.repositories.NewsRepository;
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
        return newsRepository.findAll();
    }

    public Optional<News> findById(Long id) {
        return newsRepository.findById(id);
    }

    public void save(News news) {
        newsRepository.save(news);
        log.info("News saved: {}", news);
    }

    public void delete(News news) {
        newsRepository.delete(news);
        log.info("News deleted: {}", news);
    }

    public void addNews(News news, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        if (file != null && !file.isEmpty()) {
            String resultMainImage = fileUploadService.uploadFile(file);
            news.setMainImage(resultMainImage);
            log.info("Main image uploaded: {}", resultMainImage);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            news.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Additional images uploaded: {}", newImageNames);
        }

        news.setDateOfCreation(LocalDate.now());
        save(news);
        log.info("News added: {}", news);
    }

    public void updateNews(long id, News news, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        News existingNews = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found with id: " + id));

        if (file != null && !file.isEmpty()) {
            String resultMainImage = fileUploadService.uploadFile(file);
            existingNews.setMainImage(resultMainImage);
            log.info("Main image updated: {}", resultMainImage);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            existingNews.getImages().clear();
            existingNews.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Additional images updated: {}", newImageNames);
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
        log.info("News updated: {}", existingNews);
    }
}
