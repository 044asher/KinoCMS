package com.CMS.kinoCMS.services.Pages;

import com.CMS.kinoCMS.models.Pages.Page;
import com.CMS.kinoCMS.repositories.Pages.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageService {
    private final PageRepository pageRepository;

    @Autowired
    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }
    public void save(Page page) {
        pageRepository.save(page);
    }

    public List<Page> findAll() {
       return pageRepository.findAll();
    }

    public Optional<Page> findById(long id) {
        return pageRepository.findById(id);
    }

    public List<Page> findByIsDefault(boolean isDefault) {
        return pageRepository.findAllByIsDefault(isDefault);
    }
}
