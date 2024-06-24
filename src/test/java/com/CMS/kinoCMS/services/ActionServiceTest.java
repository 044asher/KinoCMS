package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.admin.models.Action;
import com.CMS.kinoCMS.admin.repositories.ActionRepository;
import com.CMS.kinoCMS.admin.services.ActionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ActionServiceTest {
    @Mock
    private ActionRepository actionRepository;

    @InjectMocks
    private ActionService actionService;

    @Test
    public void shouldReturnActionById() {
        Long id = 1L;
        Action action = getAction(id);

        Mockito.when(actionRepository.findById(id)).thenReturn(Optional.of(action));

        Optional<Action> result = actionService.findById(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(id, result.get().getId());
        Assertions.assertEquals(action, result.get());
    }

    @Test
    public void shouldReturnAllActions() {
        List<Action> actions = getActions();

        Mockito.when(actionRepository.findAll()).thenReturn(actions);

        List<Action> result = actionService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(actions.size(), result.size());
        Assertions.assertEquals(actions, result);
    }

    @Test
    public void shouldSaveAction() {
        Action action = getAction(1L);

        actionService.save(action);

        Mockito.verify(actionRepository, Mockito.times(1)).save(action);
    }

    private Action getAction(Long id) {
        Action action = new Action();
        action.setId(id);
        action.setName("Action " + id);
        action.setDescription("Description " + id);
        action.setLink("https://www.example.com");
        action.setMainImage("img");
        action.setDateOfCreation(LocalDate.parse("2024-01-01"));
        action.setTitleSEO("Title SEO");
        action.setDescriptionSEO("Description SEO");
        action.setUrlSEO("https://www.seo.com");
        action.setKeywordsSEO("Keywords SEO");
        return action;
    }

    private List<Action> getActions() {
        Action firstAction = getAction(1L);
        Action secondAction = getAction(2L);
        return List.of(firstAction, secondAction);
    }
}
