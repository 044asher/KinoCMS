package com.CMS.kinoCMS.admin.services.DefaultInitializers;

import com.CMS.kinoCMS.models.Banners.BannersAndSliders;
import com.CMS.kinoCMS.repositories.BannersAndSlidersRepository;
import com.CMS.kinoCMS.services.DefaultInitializers.DefaultBannersAndSlidersInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultBannersAndSlidersInitializerTest {

    @InjectMocks
    private DefaultBannersAndSlidersInitializer defaultBannersAndSlidersInitializer;

    @Mock
    private BannersAndSlidersRepository bannersAndSlidersRepository;

    @Test
    void testInitializeDefaultBannersAndSliders_WhenRepositoryIsEmpty() {
        when(bannersAndSlidersRepository.count()).thenReturn(0L);

        defaultBannersAndSlidersInitializer.initializeDefaultBannersAndSliders();

        verify(bannersAndSlidersRepository).save(argThat(
                bannersAndSliders -> "default_background.jpg".equals(bannersAndSliders.getBackground())
        ));
    }

    @Test
    void testInitializeDefaultBannersAndSliders_WhenRepositoryIsNotEmpty() {
        when(bannersAndSlidersRepository.count()).thenReturn(5L);

        defaultBannersAndSlidersInitializer.initializeDefaultBannersAndSliders();

        verify(bannersAndSlidersRepository, never()).save(any(BannersAndSliders.class));
    }
}