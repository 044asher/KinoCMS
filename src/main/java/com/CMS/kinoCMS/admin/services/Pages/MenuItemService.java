package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.admin.models.Pages.MenuItem;
import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.repositories.Pages.MenuItemRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> findByPage(Page page) {
        log.info("Finding menu items for page: {}", page);
        return menuItemRepository.findByPage(page);
    }

    public void save(MenuItem menuItem) {
        log.info("Saving menu item: {}", menuItem);
        menuItemRepository.save(menuItem);
    }

    public void deleteById(Long id) {
        log.info("Deleting menu item with id: {}", id);
        menuItemRepository.deleteById(id);
    }

    public MenuItem findById(long itemId) {
        log.info("Finding menu item with id: {}", itemId);
        return menuItemRepository.findById(itemId).orElse(null);
    }

    public List<MenuItem> findAll() {
        log.info("Fetching all menu items");
        return menuItemRepository.findAll();
    }
}
