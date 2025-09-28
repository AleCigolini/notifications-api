package br.com.fiap.messages.presentation.impl;

import br.com.fiap.messages.application.controller.MessagesController;
import br.com.fiap.messages.common.dto.request.MessagesRequestDTO;
import br.com.fiap.messages.common.dto.response.MessagesResponseDTO;
import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.StatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagesRestControllerImplTest {

    @Mock
    private MessagesController messagesController;

    @InjectMocks
    private MessagesRestControllerImpl messagesRestController;

    private MessagesRequestDTO requestDTO;
    private MessagesResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        requestDTO = new MessagesRequestDTO();
        requestDTO.setContent("Test message");
        requestDTO.setType(MessageType.P);
        requestDTO.setRecipient("user123");
        requestDTO.setStatus(StatusType.INFO);

        responseDTO = new MessagesResponseDTO();
        responseDTO.setId("msg-123");
        responseDTO.setContent("Test message");
        responseDTO.setType(MessageType.P);
        responseDTO.setRecipient("user123");
        responseDTO.setStatus(StatusType.INFO);
        responseDTO.setCreated_at(OffsetDateTime.now());
    }

    @Test
    void testSaveMessage_Success() {
        when(messagesController.save(any(MessagesRequestDTO.class))).thenReturn(responseDTO);

        MessagesResponseDTO result = messagesRestController.save(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getContent(), result.getContent());
        assertEquals(responseDTO.getType(), result.getType());
        assertEquals(responseDTO.getRecipient(), result.getRecipient());
        assertEquals(responseDTO.getStatus(), result.getStatus());
        assertEquals(responseDTO.getCreated_at(), result.getCreated_at());

        verify(messagesController, times(1)).save(requestDTO);
    }

    @Test
    void testSaveMessage_NullRequest() {
        MessagesRequestDTO nullRequest = null;
        when(messagesController.save(nullRequest)).thenReturn(null);

        MessagesResponseDTO result = messagesRestController.save(nullRequest);

        assertEquals(null, result);

        verify(messagesController, times(1)).save(null);
    }
}
