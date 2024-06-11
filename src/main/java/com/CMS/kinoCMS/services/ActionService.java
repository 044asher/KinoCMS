package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.models.Action;
import com.CMS.kinoCMS.repositories.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActionService {
    private final ActionRepository actionRepository;

    @Autowired
    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public List<Action> findAll() {
       return actionRepository.findAll();
    }

    public Optional<Action> findById(long id) {
        return actionRepository.findById(id);
    }

    public void save(Action action) {
        actionRepository.save(action);
    }
}
