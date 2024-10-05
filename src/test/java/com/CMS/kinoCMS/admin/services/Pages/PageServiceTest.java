package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.models.Pages.Contact;
import com.CMS.kinoCMS.models.Pages.MainPage;
import com.CMS.kinoCMS.models.Pages.MenuItem;
import com.CMS.kinoCMS.models.Pages.Page;
import com.CMS.kinoCMS.repositories.Pages.PageRepository;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.Pages.ContactsPageService;
import com.CMS.kinoCMS.services.Pages.MainPageService;
import com.CMS.kinoCMS.services.Pages.MenuItemService;
import com.CMS.kinoCMS.services.Pages.PageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PageServiceTest {

    @Mock
    private PageRepository pageRepository;

    @Mock
    private FileUploadService fileUploadService;

    @Mock
    private MenuItemService menuItemService;

    @Mock
    private MainPageService mainPageService;

    @Mock
    private ContactsPageService contactsPageService;

    @InjectMocks
    private PageService pageService;

    @Test
    void testSave() {
        Page page = new Page();
        pageService.save(page);
        verify(pageRepository, times(1)).save(page);
    }

    @Test
    void testFindAll() {
        List<Page> pages = List.of(new Page(), new Page());
        when(pageRepository.findAll()).thenReturn(pages);

        List<Page> result = pageService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(pageRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Page page = new Page();
        when(pageRepository.findById(1L)).thenReturn(Optional.of(page));

        Optional<Page> result = pageService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(page, result.get());
        verify(pageRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIsDefault() {
        List<Page> pages = List.of(new Page(), new Page());
        when(pageRepository.findAllByIsDefault(true)).thenReturn(pages);

        List<Page> result = pageService.findByIsDefault(true);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(pageRepository, times(1)).findAllByIsDefault(true);
    }

    @Test
    void testFindByName() {
        Page page = new Page();
        when(pageRepository.findByName("Test Page")).thenReturn(Optional.of(page));

        Optional<Page> result = pageService.findByName("Test Page");

        assertTrue(result.isPresent());
        assertEquals(page, result.get());
        verify(pageRepository, times(1)).findByName("Test Page");
    }

    @Test
    void testAddNewPage() throws IOException {
        Page newPage = new Page();
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};
        when(file.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(file)).thenReturn("mainImage.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("image1.jpg", "image2.jpg"));

        pageService.addNewPage(newPage, file, additionalFiles);

        assertEquals("mainImage.jpg", newPage.getMainImage());
        assertEquals(2, newPage.getImages().size());
        assertEquals(LocalDate.now(), newPage.getDateOfCreation());
        assertFalse(newPage.isNotActive());
        assertFalse(newPage.isDefault());
        verify(pageRepository, times(1)).save(newPage);
    }

    @Test
    void testUpdatePage() throws IOException {
        Page existingPage = new Page();
        existingPage.setId(1L);
        Page updatedPage = new Page();
        updatedPage.setName("Updated Name");
        updatedPage.setDescription("Updated Description");
        updatedPage.setDescriptionSEO("Updated Description SEO");
        updatedPage.setTitleSEO("Updated Title SEO");
        updatedPage.setUrlSEO("Updated URL SEO");
        updatedPage.setKeywordsSEO("Updated Keywords SEO");
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};
        when(pageRepository.findById(1L)).thenReturn(Optional.of(existingPage));
        when(file.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(file)).thenReturn("mainImage.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("image1.jpg", "image2.jpg"));

        pageService.updatePage(1L, updatedPage, file, additionalFiles);

        assertEquals("Updated Name", existingPage.getName());
        assertEquals("Updated Description", existingPage.getDescription());
        assertEquals("Updated Description SEO", existingPage.getDescriptionSEO());
        assertEquals("Updated Title SEO", existingPage.getTitleSEO());
        assertEquals("Updated URL SEO", existingPage.getUrlSEO());
        assertEquals("Updated Keywords SEO", existingPage.getKeywordsSEO());
        assertEquals("mainImage.jpg", existingPage.getMainImage());
        assertEquals(2, existingPage.getImages().size());
        verify(pageRepository, times(1)).findById(1L);
        verify(pageRepository, times(1)).save(existingPage);
    }

    @Test
    void testUpdatePage_PageNotFound() {
        Long nonExistentPageId = 999L;
        Page updatedPage = new Page();
        updatedPage.setName("Updated Name");
        updatedPage.setDescription("Updated Description");

        when(pageRepository.findById(nonExistentPageId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pageService.updatePage(nonExistentPageId, updatedPage, null, null));

        assertEquals("Page Not Found", exception.getMessage());
        verify(pageRepository, times(1)).findById(nonExistentPageId);
    }


    @Test
    void testAddMenuItemToPage() {
        Page page = new Page();
        page.setName("Кафе-Бар");
        when(pageRepository.findById(1L)).thenReturn(Optional.of(page));

        pageService.addMenuItemToPage(1L, "Item Name", 10.0, "Ingredients");

        verify(menuItemService, times(1)).save(any(MenuItem.class));
    }

    @Test
    void testAddMenuItemToPage_PageNotFound() {
        when(pageRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pageService.addMenuItemToPage(1L, "Item Name", 10.0, "Ingredients"));

        assertEquals("Page Not Found", exception.getMessage());
        verify(menuItemService, times(0)).save(any(MenuItem.class));
    }

    @Test
    void testAddMenuItemToPage_NotCafeBarPage() {
        Page page = new Page();
        page.setName("Не Кафе-Бар");

        when(pageRepository.findById(1L)).thenReturn(Optional.of(page));


        pageService.addMenuItemToPage(1L, "Item Name", 10.0, "Ingredients");

        verify(menuItemService, times(0)).save(any(MenuItem.class));
    }


    @Test
    void testChangeStatus_MainPage() {
        MainPage mainPage = new MainPage();
        mainPage.setNotActive(true);

        pageService.changeStatus(mainPage);

        assertFalse(mainPage.isNotActive());
        verify(mainPageService, times(1)).save(mainPage);
    }

    @Test
    void testChangeStatus_Contact() {
        Contact contact = new Contact();
        contact.setNotActive(true);

        pageService.changeStatus(contact);

        assertFalse(contact.isNotActive());
        verify(contactsPageService, times(1)).save(contact);
    }

    @Test
    void testChangeStatus_Page() {
        Page page = new Page();
        page.setNotActive(true);

        pageService.changeStatus(page);

        assertFalse(page.isNotActive());
        verify(pageRepository, times(1)).save(page);
    }

    @Test
    void testGetPageWithCheck() {
        Page page = new Page();
        page.setId(1L);
        page.setName("Test page");

        Model model = mock(Model.class);
        when(pageRepository.findByName("Test Page")).thenReturn(Optional.of(page));

        Optional<Page> result = pageService.getPageWithCheck("Test Page", model);

        assertTrue(result.isPresent());
        verify(model, times(1)).addAttribute("page", page);
    }

    @Test
    void testGetPageWithCheck_ActivePage() {
        Page page = new Page();
        page.setId(1L);
        page.setName("Test Page");
        page.setNotActive(false);

        Model model = mock(Model.class);
        when(pageRepository.findByName("Test Page")).thenReturn(Optional.of(page));

        Optional<Page> result = pageService.getPageWithCheck("Test Page", model);

        assertTrue(result.isPresent());
        assertEquals(page, result.get());
        verify(model, times(1)).addAttribute("page", page);
        verifyNoMoreInteractions(model);
    }


    @Test
    void testAddNewPage_FileUploadException() throws IOException {
        Page newPage = new Page();
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};
        when(file.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(file)).thenThrow(new IOException("File upload error"));

        IOException exception = assertThrows(IOException.class, () -> pageService.addNewPage(newPage, file, additionalFiles));

        assertEquals("File upload error", exception.getMessage());
        verify(pageRepository, times(0)).save(newPage);
    }

    @Test
    void testUpdatePage_FileUploadException() throws IOException {
        Page existingPage = new Page();
        existingPage.setId(1L);
        Page updatedPage = new Page();
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};
        when(pageRepository.findById(1L)).thenReturn(Optional.of(existingPage));
        when(file.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(file)).thenThrow(new IOException("File upload error"));

        IOException exception = assertThrows(IOException.class, () -> pageService.updatePage(1L, updatedPage, file, additionalFiles));

        assertEquals("File upload error", exception.getMessage());
        verify(pageRepository, times(0)).save(existingPage);
    }

    @Test
    void testChangeStatus_UnsupportedType() {
        assertThrows(IllegalArgumentException.class, () -> pageService.changeStatus(new Object()));
    }

    @Test
    void testPageDeleteById_PageNotFound() {
        when(pageRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pageService.pageDeleteById(1L));

        assertEquals("Page Not Found", exception.getMessage());
        verify(pageRepository, times(0)).delete(any(Page.class));
    }

    @Test
    void testPageDeleteById_Success() {
        Page page = new Page();
        when(pageRepository.findById(1L)).thenReturn(Optional.of(page));

        pageService.pageDeleteById(1L);

        verify(pageRepository, times(1)).delete(page);
    }


    @Test
    void testGetPageWithCheck_PageIsNotActive() {
        Page page = new Page();
        page.setId(1L);
        page.setName("Test Page");
        page.setNotActive(true);

        Model model = mock(Model.class);
        when(pageRepository.findByName("Test Page")).thenReturn(Optional.of(page));

        Optional<Page> result = pageService.getPageWithCheck("Test Page", model);

        assertTrue(result.isPresent());
        verify(model, times(1)).addAttribute("page", page);
        verifyNoMoreInteractions(model);
    }

    @Test
    void testExistsByName() {
        when(pageRepository.existsByName("Test Page")).thenReturn(true);

        boolean result = pageService.existsByName("Test Page");

        assertTrue(result);
        verify(pageRepository, times(1)).existsByName("Test Page");
    }

    @Test
    void testPopulateDefaultSEOFields() {
        Page page = new Page();

        pageService.populateDefaultSEOFields(page);

        assertEquals("default-url", page.getUrlSEO());
        assertEquals("Default Title", page.getTitleSEO());
        assertEquals("Default Description", page.getDescriptionSEO());
        assertEquals("default, keywords", page.getKeywordsSEO());
        assertEquals("Default Page Description", page.getDescription());
    }
}

