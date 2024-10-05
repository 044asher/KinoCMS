package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.models.News;
import com.CMS.kinoCMS.repositories.NewsRepository;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.NewsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private FileUploadService fileUploadService;

    @InjectMocks
    private NewsService newsService;

    @Test
    public void testFindAll() {
        List<News> newsList = new ArrayList<>();
        when(newsRepository.findAll()).thenReturn(newsList);

        List<News> result = newsService.findAll();
        assertEquals(newsList, result);
        verify(newsRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        News news = new News();
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));

        Optional<News> result = newsService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(news, result.get());
        verify(newsRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<News> result = newsService.findById(1L);
        assertFalse(result.isPresent());
        verify(newsRepository, times(1)).findById(1L);
    }

    @Test
    public void testSave() {
        News news = new News();
        newsService.save(news);
        verify(newsRepository, times(1)).save(news);
    }

    @Test
    public void testDelete() {
        News news = new News();
        newsService.delete(news);
        verify(newsRepository, times(1)).delete(news);
    }

    @Test
    public void testAddNews() throws IOException {
        News news = new News();
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = new MultipartFile[]{mock(MultipartFile.class), mock(MultipartFile.class)};

        when(file.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(file)).thenReturn("mainImage.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("image1.jpg", "image2.jpg"));

        newsService.addNews(news, file, additionalFiles);

        assertEquals("mainImage.jpg", news.getMainImage());
        assertEquals(2, news.getImages().size());
        assertEquals("image1.jpg", news.getImages().get(0));
        assertEquals("image2.jpg", news.getImages().get(1));
        assertEquals(LocalDate.now(), news.getDateOfCreation());
        verify(newsRepository, times(1)).save(news);
    }

    @Test
    public void testAddNews_WithNullFile() throws IOException {
        News news = new News();
        MultipartFile[] additionalFiles = new MultipartFile[]{mock(MultipartFile.class), mock(MultipartFile.class)};

        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("image1.jpg", "image2.jpg"));

        newsService.addNews(news, null, additionalFiles);

        assertNull(news.getMainImage());
        assertEquals(2, news.getImages().size());
        assertEquals("image1.jpg", news.getImages().get(0));
        assertEquals("image2.jpg", news.getImages().get(1));
        assertEquals(LocalDate.now(), news.getDateOfCreation());
        verify(newsRepository, times(1)).save(news);
    }

    @Test
    public void testAddNews_WithEmptyFiles() throws IOException {
        News news = new News();
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = new MultipartFile[]{};

        when(file.isEmpty()).thenReturn(true);

        newsService.addNews(news, file, additionalFiles);

        assertNull(news.getMainImage());
        assertTrue(news.getImages().isEmpty());
        assertEquals(LocalDate.now(), news.getDateOfCreation());
        verify(newsRepository, times(1)).save(news);
    }

    @Test
    public void testUpdateNews() throws IOException {
        News existingNews = new News();
        existingNews.setImages(new ArrayList<>());
        when(newsRepository.findById(1L)).thenReturn(Optional.of(existingNews));

        News updatedNews = new News();
        updatedNews.setName("Updated Name");
        updatedNews.setDescription("Updated Description");
        updatedNews.setDateOfCreation(LocalDate.now().minusDays(1));
        updatedNews.setLink("https://example.com");
        updatedNews.setUrlSEO("updated-url-seo");
        updatedNews.setKeywordsSEO("updated-keywords-seo");
        updatedNews.setDescriptionSEO("updated-description-seo");
        updatedNews.setTitleSEO("updated-title-seo");

        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = new MultipartFile[]{mock(MultipartFile.class), mock(MultipartFile.class)};

        when(file.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(file)).thenReturn("updatedMainImage.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("updatedImage1.jpg", "updatedImage2.jpg"));

        newsService.updateNews(1L, updatedNews, file, additionalFiles);

        assertEquals("Updated Name", existingNews.getName());
        assertEquals("Updated Description", existingNews.getDescription());
        assertEquals("updatedMainImage.jpg", existingNews.getMainImage());
        assertEquals(2, existingNews.getImages().size());
        assertEquals("updatedImage1.jpg", existingNews.getImages().get(0));
        assertEquals("updatedImage2.jpg", existingNews.getImages().get(1));
        assertEquals("https://example.com", existingNews.getLink());
        assertEquals("updated-url-seo", existingNews.getUrlSEO());
        assertEquals("updated-keywords-seo", existingNews.getKeywordsSEO());
        assertEquals("updated-description-seo", existingNews.getDescriptionSEO());
        assertEquals("updated-title-seo", existingNews.getTitleSEO());
        verify(newsRepository, times(1)).save(existingNews);
    }

    @Test
    public void testUpdateNews_WithNullFile() throws IOException {
        News existingNews = new News();
        existingNews.setImages(new ArrayList<>());
        when(newsRepository.findById(1L)).thenReturn(Optional.of(existingNews));

        News updatedNews = new News();
        updatedNews.setName("Updated Name");
        updatedNews.setDescription("Updated Description");

        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = new MultipartFile[]{mock(MultipartFile.class), mock(MultipartFile.class)};

        when(file.isEmpty()).thenReturn(true);
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("updatedImage1.jpg", "updatedImage2.jpg"));

        newsService.updateNews(1L, updatedNews, file, additionalFiles);

        assertEquals("Updated Name", existingNews.getName());
        assertEquals("Updated Description", existingNews.getDescription());
        assertNull(existingNews.getMainImage());
        assertEquals(2, existingNews.getImages().size());
        assertEquals("updatedImage1.jpg", existingNews.getImages().get(0));
        assertEquals("updatedImage2.jpg", existingNews.getImages().get(1));
        verify(newsRepository, times(1)).save(existingNews);
    }

    @Test
    public void testUpdateNews_WithEmptyFiles() throws IOException {
        News existingNews = new News();
        existingNews.setImages(new ArrayList<>());
        when(newsRepository.findById(1L)).thenReturn(Optional.of(existingNews));

        News updatedNews = new News();
        updatedNews.setName("Updated Name");
        updatedNews.setDescription("Updated Description");

        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = new MultipartFile[]{};

        when(file.isEmpty()).thenReturn(true);

        newsService.updateNews(1L, updatedNews, file, additionalFiles);

        assertEquals("Updated Name", existingNews.getName());
        assertEquals("Updated Description", existingNews.getDescription());
        assertNull(existingNews.getMainImage());
        assertTrue(existingNews.getImages().isEmpty());
        verify(newsRepository, times(1)).save(existingNews);
    }

    @Test
    public void testFindByNotActive() {
        List<News> newsList = new ArrayList<>();
        when(newsRepository.findByNotActive(true)).thenReturn(newsList);

        List<News> result = newsService.findByNotActive(true);
        assertEquals(newsList, result);
        verify(newsRepository, times(1)).findByNotActive(true);
    }
}
