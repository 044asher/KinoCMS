package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.News;
import com.CMS.kinoCMS.admin.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
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
    }
    public void delete(News news) {
        newsRepository.delete(news);
    }

    public void addNews(News news, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        if (file != null && !file.isEmpty()) {
            String resultMainImage = fileUploadService.uploadFile(file);
            news.setMainImage(resultMainImage);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            news.getImages().addAll(newImageNames.stream().limit(5).toList());
        }

        news.setDateOfCreation(LocalDate.now());
        save(news);
    }

    public void updateNews(long id, News news, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        News existingNews = findById(id).orElseThrow(() -> new IllegalArgumentException("News not found"));

        if (file != null && !file.isEmpty()) {
            String resultMainImage = fileUploadService.uploadFile(file);
            existingNews.setMainImage(resultMainImage);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            existingNews.getImages().clear();
            existingNews.getImages().addAll(newImageNames.stream().limit(5).toList());
        }

        existingNews.setName(news.getName());
        existingNews.setDescription(news.getDescription());
        if (news.getDateOfCreation() != null) {
            existingNews.setDateOfCreation(news.getDateOfCreation());
        }
        existingNews.setLink(news.getLink());
        existingNews.setUrlSEO(news.getUrlSEO());
        existingNews.setKeywordsSEO(news.getKeywordsSEO());
        existingNews.setDescriptionSEO(news.getDescriptionSEO());
        existingNews.setTitleSEO(news.getTitleSEO());

        save(existingNews);
    }
}
