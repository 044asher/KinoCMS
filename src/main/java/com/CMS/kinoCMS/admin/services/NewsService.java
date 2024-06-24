package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.News;
import com.CMS.kinoCMS.admin.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
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
}
