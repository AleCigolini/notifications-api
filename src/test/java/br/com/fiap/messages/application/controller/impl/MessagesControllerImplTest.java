package br.com.fiap.messages.application.controller.impl;

import br.com.fiap.messages.application.mapper.DatabaseMessagesMapper;
import br.com.fiap.messages.application.mapper.EventMapper;
import br.com.fiap.messages.application.mapper.RequestMessagesMapper;
import br.com.fiap.messages.application.presenter.MessagesPresenter;
import br.com.fiap.messages.application.usecase.ConsultMessagesUseCase;
import br.com.fiap.messages.application.usecase.EventSenderUseCase;
import br.com.fiap.messages.application.usecase.SaveMessagesUseCase;
import br.com.fiap.messages.common.dto.request.MessagesRequestDTO;
import br.com.fiap.messages.common.dto.response.MessagesResponseDTO;
import br.com.fiap.messages.common.interfaces.EventRedis;
import br.com.fiap.messages.common.interfaces.MessagesDatabase;
import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagesControllerImplTest {

    @Mock
    private MessagesDatabase messagesDatabase;

    @Mock
    private DatabaseMessagesMapper databaseMessagesMapper;

    @Mock
    private RequestMessagesMapper requestMessagesMapper;

    @Mock
    private MessagesPresenter messagesPresenter;

    @Mock
    private EventRedis eventRedis;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private SaveMessagesUseCase saveMessagesUseCase;

    @Mock
    private EventSenderUseCase eventSenderUseCase;

    private MessagesControllerImpl messagesController;

    private MessagesRequestDTO requestDTO;
    private Messages domainMessage;
    private MessagesResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        messagesController = new MessagesControllerImpl(
            messagesDatabase,
            databaseMessagesMapper,
            requestMessagesMapper,
            messagesPresenter,
            eventRedis,
            eventMapper
        );

        try {
            java.lang.reflect.Field saveUseCaseField = MessagesControllerImpl.class.getDeclaredField("saveMessagesUseCase");
            saveUseCaseField.setAccessible(true);
            saveUseCaseField.set(messagesController, saveMessagesUseCase);

            java.lang.reflect.Field eventSenderUseCaseField = MessagesControllerImpl.class.getDeclaredField("eventSenderUseCase");
            eventSenderUseCaseField.setAccessible(true);
            eventSenderUseCaseField.set(messagesController, eventSenderUseCase);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set mocked use cases", e);
        }

        requestDTO = new MessagesRequestDTO();
        requestDTO.setContent("Test message content");
        requestDTO.setType(MessageType.P);
        requestDTO.setRecipient("user123");
        requestDTO.setChannel("email");

        domainMessage = new Messages();
        domainMessage.setId(UUID.randomUUID());
        domainMessage.setContent("Test message content");
        domainMessage.setCreatedAt(OffsetDateTime.now());
        domainMessage.setType(MessageType.P);
        domainMessage.setRecipient("user123");
        domainMessage.setChannel("email");

        responseDTO = new MessagesResponseDTO();
        responseDTO.setId(domainMessage.getId().toString());
        responseDTO.setContent("Test message content");
        responseDTO.setCreated_at(domainMessage.getCreatedAt());
        responseDTO.setType(MessageType.P);
        responseDTO.setRecipient("user123");
        responseDTO.setChannel("email");
    }

    @Test
    void testSaveMessage_Success() {
        when(requestMessagesMapper.toDomain(requestDTO)).thenReturn(domainMessage);
        when(saveMessagesUseCase.save(domainMessage)).thenReturn(domainMessage);
        when(messagesPresenter.toResponse(domainMessage)).thenReturn(responseDTO);
        doNothing().when(eventSenderUseCase).publishEvent(domainMessage);

        MessagesResponseDTO result = messagesController.save(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getContent(), result.getContent());
        assertEquals(responseDTO.getType(), result.getType());
        assertEquals(responseDTO.getRecipient(), result.getRecipient());
        assertEquals(responseDTO.getChannel(), result.getChannel());

        verify(requestMessagesMapper, times(1)).toDomain(requestDTO);
        verify(saveMessagesUseCase, times(1)).save(domainMessage);
        verify(eventSenderUseCase, times(1)).publishEvent(domainMessage);
        verify(messagesPresenter, times(1)).toResponse(domainMessage);
    }

    @Test
    void testSaveMessage_NullRequest() {
        MessagesRequestDTO nullRequest = null;
        Messages emptyMessage = new Messages();
        when(requestMessagesMapper.toDomain(null)).thenReturn(emptyMessage);
        when(saveMessagesUseCase.save(emptyMessage)).thenReturn(emptyMessage);
        when(messagesPresenter.toResponse(emptyMessage)).thenReturn(new MessagesResponseDTO());

        MessagesResponseDTO result = messagesController.save(nullRequest);

        assertNotNull(result);

        verify(requestMessagesMapper, times(1)).toDomain(null);
        verify(saveMessagesUseCase, times(1)).save(emptyMessage);
        verify(eventSenderUseCase, times(1)).publishEvent(emptyMessage);
    }
}
