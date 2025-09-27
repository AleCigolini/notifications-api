package br.com.fiap.messages.application.usecase.impl;

import br.com.fiap.messages.application.gateway.MessagesGateway;
import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveMessagesUseCaseImplTest {

    @Mock
    private MessagesGateway messagesGateway;

    @InjectMocks
    private SaveMessagesUseCaseImpl saveMessagesUseCase;

    private Messages testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new Messages();
        testMessage.setId(UUID.randomUUID());
        testMessage.setContent("Test message content");
        testMessage.setCreatedAt(OffsetDateTime.now());
        testMessage.setType(MessageType.P);
        testMessage.setRecipient("user123");
        testMessage.setChannel("email");
    }

    @Test
    void testSaveMessage_Success() {
        when(messagesGateway.save(testMessage)).thenReturn(testMessage);

        Messages result = saveMessagesUseCase.save(testMessage);

        assertEquals(testMessage.getId(), result.getId());
        assertEquals(testMessage.getContent(), result.getContent());
        assertEquals(testMessage.getCreatedAt(), result.getCreatedAt());
        assertEquals(testMessage.getType(), result.getType());
        assertEquals(testMessage.getRecipient(), result.getRecipient());
        assertEquals(testMessage.getChannel(), result.getChannel());

        verify(messagesGateway, times(1)).save(testMessage);
    }

    @Test
    void testSaveMessage_NullMessage() {
        Messages nullMessage = null;
        when(messagesGateway.save(null)).thenReturn(null);

        Messages result = saveMessagesUseCase.save(nullMessage);

        assertNull(result);

        verify(messagesGateway, times(1)).save(null);
    }

    @Test
    @DisplayName("Should handle empty message")
    void testSaveMessage_EmptyMessage() {
        Messages emptyMessage = new Messages();
        when(messagesGateway.save(emptyMessage)).thenReturn(emptyMessage);

        Messages result = saveMessagesUseCase.save(emptyMessage);

        assertEquals(emptyMessage, result);

        verify(messagesGateway, times(1)).save(emptyMessage);
    }
}
