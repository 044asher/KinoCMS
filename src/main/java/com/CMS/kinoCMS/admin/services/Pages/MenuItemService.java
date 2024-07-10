package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.admin.models.Pages.MenuItem;
import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.repositories.Pages.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> findByPage(Page page) {
        return menuItemRepository.findByPage(page);
    }

    public MenuItem save(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }

    public MenuItem findById(long itemId) {
        return menuItemRepository.findById(itemId).orElse(null);
    }

    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }
}
