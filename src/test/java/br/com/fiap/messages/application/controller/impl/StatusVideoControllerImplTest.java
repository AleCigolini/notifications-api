package br.com.fiap.messages.application.controller.impl;

import br.com.fiap.messages.application.mapper.DatabaseMessagesMapper;
import br.com.fiap.messages.application.mapper.EventMapper;
import br.com.fiap.messages.application.mapper.StatusMessagesMapper;
import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;
import br.com.fiap.messages.common.dto.request.StatusVideoDTO;
import br.com.fiap.messages.common.interfaces.EventRedis;
import br.com.fiap.messages.common.interfaces.MessagesDatabase;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusVideoControllerImplTest {
    @Mock
    private MessagesDatabase messagesDatabase;
    @Mock
    private DatabaseMessagesMapper databaseMessagesMapper;
    @Mock
    private StatusMessagesMapper statusMessagesMapper;
    @Mock
    private EventRedis eventRedis;
    @Mock
    private EventMapper eventMapper;
    @Mock
    private StatusVideoDTO statusVideoDTO;
    @Mock
    private Messages messages;
    @Mock
    private JpaMessagesEntity messagesEntity;

    private StatusVideoControllerImpl statusVideoController;

    @BeforeEach
    void setUp() {
        statusVideoController = new StatusVideoControllerImpl(
                messagesDatabase,
                databaseMessagesMapper,
                statusMessagesMapper,
                eventRedis,
                eventMapper
        );
    }

    @Test
    void shouldMapAndSaveAndPublishEventWhenNotifyStatusVideoIsCalled() {
        when(statusMessagesMapper.toDomain(statusVideoDTO)).thenReturn(messages);
        when(databaseMessagesMapper.toEntity(messages)).thenReturn(messagesEntity);
        when(messagesDatabase.save(messagesEntity)).thenReturn(messagesEntity);
        when(databaseMessagesMapper.toDomain(messagesEntity)).thenReturn(messages);

        statusVideoController.notifyStatusVideo(statusVideoDTO);

        verify(statusMessagesMapper, times(1)).toDomain(statusVideoDTO);
        verify(databaseMessagesMapper, times(1)).toEntity(messages);
        verify(messagesDatabase, times(1)).save(messagesEntity);
        verify(databaseMessagesMapper, times(1)).toDomain(messagesEntity);
    }

    @Test
    void shouldPropagateExceptionIfSaveFails() {
        when(statusMessagesMapper.toDomain(statusVideoDTO)).thenReturn(messages);
        when(databaseMessagesMapper.toEntity(messages)).thenReturn(messagesEntity);
        when(messagesDatabase.save(messagesEntity)).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () ->
                statusVideoController.notifyStatusVideo(statusVideoDTO)
        );
    }

}
