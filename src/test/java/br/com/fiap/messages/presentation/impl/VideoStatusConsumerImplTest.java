package br.com.fiap.messages.presentation.impl;

import br.com.fiap.messages.application.controller.StatusVideoController;
import br.com.fiap.messages.common.dto.request.StatusVideoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoStatusConsumerImplTest {

    @Mock
    private StatusVideoController statusVideoController;

    @InjectMocks
    private VideoStatusConsumerImpl videoStatusConsumer;

    private StatusVideoDTO statusVideoDTO;

    @BeforeEach
    void setUp() {
        statusVideoDTO = mock(StatusVideoDTO.class);
    }

    @Test
    void shouldNotifyStatusVideoWhenMessageIsConsumed() {
        videoStatusConsumer.videoStatusConsume(statusVideoDTO);
        verify(statusVideoController, times(1)).notifyStatusVideo(statusVideoDTO);
    }

    @Test
    void shouldThrowExceptionAndLogErrorWhenNotifyStatusVideoFails() {
        doThrow(new RuntimeException("Test exception")).when(statusVideoController).notifyStatusVideo(statusVideoDTO);
        assertThrows(RuntimeException.class, () -> videoStatusConsumer.videoStatusConsume(statusVideoDTO));
        verify(statusVideoController, times(1)).notifyStatusVideo(statusVideoDTO);
    }
}

