package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.models.Pages.MenuItem;
import com.CMS.kinoCMS.models.Pages.Page;
import com.CMS.kinoCMS.repositories.Pages.MenuItemRepository;
import com.CMS.kinoCMS.services.Pages.MenuItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuItemService menuItemService;

    @Test
    void testFindByPage() {
        Page page = new Page();
        List<MenuItem> menuItems = List.of(new MenuItem(), new MenuItem());
        when(menuItemRepository.findByPage(page)).thenReturn(menuItems);

        List<MenuItem> result = menuItemService.findByPage(page);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(menuItemRepository, times(1)).findByPage(page);
    }

    @Test
    void testSave() {
        MenuItem menuItem = new MenuItem();

        menuItemService.save(menuItem);

        verify(menuItemRepository, times(1)).save(menuItem);
    }

    @Test
    void testDeleteById() {
        Long id = 1L;

        menuItemService.deleteById(id);

        verify(menuItemRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindById() {
        MenuItem menuItem = new MenuItem();
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        MenuItem result = menuItemService.findById(1L);

        assertNotNull(result);
        assertEquals(menuItem, result);
        verify(menuItemRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        MenuItem result = menuItemService.findById(1L);

        assertNull(result);
        verify(menuItemRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        List<MenuItem> menuItems = List.of(new MenuItem(), new MenuItem());
        when(menuItemRepository.findAll()).thenReturn(menuItems);

        List<MenuItem> result = menuItemService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(menuItemRepository, times(1)).findAll();
    }
}
