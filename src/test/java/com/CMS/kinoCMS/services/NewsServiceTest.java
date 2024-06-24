package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.admin.models.News;
import com.CMS.kinoCMS.admin.repositories.NewsRepository;
import com.CMS.kinoCMS.admin.services.NewsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsService newsService;

    @Test
    public void shouldReturnAllNews() {
        News news1 = new News();
        News news2 = new News();
        List<News> newsList = List.of(news1, news2);

        Mockito.when(newsRepository.findAll()).thenReturn(newsList);

        List<News> result = newsService.findAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(newsList, result);
    }

    @Test
    public void shouldReturnNewsById() {
        Long id = 1L;
        News news = new News();
        news.setId(id);

        Mockito.when(newsRepository.findById(id)).thenReturn(Optional.of(news));

        Optional<News> result = newsService.findById(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(news, result.get());
    }

    @Test
    public void shouldSaveNews() {
        News news = new News();
        news.setId(1L);

        newsService.save(news);

        Mockito.verify(newsRepository, Mockito.times(1)).save(news);
    }

    @Test
    public void shouldDeleteNews() {
        News news = new News();
        news.setId(1L);

        newsService.delete(news);

        Mockito.verify(newsRepository, Mockito.times(1)).delete(news);
    }
}
