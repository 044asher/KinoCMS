package com.CMS.kinoCMS.services.Pages;

import com.CMS.kinoCMS.models.Pages.MenuItem;
import com.CMS.kinoCMS.models.Pages.Page;
import com.CMS.kinoCMS.repositories.Pages.MenuItemRepository;
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
        log.info("Start MenuItemService - findByPage for page: {}", page.getName());
        List<MenuItem> menuItems = menuItemRepository.findByPage(page);
        log.info("Successfully executed MenuItemService - findByPage for page: {}", page.getName());
        return menuItems;
    }

    public void save(MenuItem menuItem) {
        log.info("Start MenuItemService - save for menu item: {}", menuItem.getItemName());
        menuItemRepository.save(menuItem);
        log.info("Successfully executed MenuItemService - save for menu item: {}", menuItem.getItemName());
    }

    public void deleteById(Long id) {
        log.info("Start MenuItemService - deleteById with id: {}", id);
        menuItemRepository.deleteById(id);
        log.info("Successfully executed MenuItemService - deleteById with id: {}", id);
    }

    public MenuItem findById(long itemId) {
        log.info("Start MenuItemService - findById with id: {}", itemId);
        MenuItem menuItem = menuItemRepository.findById(itemId).orElse(null);
        log.info("Successfully executed MenuItemService - findById with id: {}", itemId);
        return menuItem;
    }

    public List<MenuItem> findAll() {
        log.info("Start MenuItemService - findAll");
        List<MenuItem> menuItems = menuItemRepository.findAll();
        log.info("Successfully executed MenuItemService - findAll");
        return menuItems;
    }
}
