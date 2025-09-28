package br.com.fiap.messages.application.mapper.impl;

import br.com.fiap.messages.common.dto.request.StatusVideoDTO;
import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.Messages;
import br.com.fiap.messages.domain.StatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusMessagesMapperImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StatusMessagesMapperImpl statusMessagesMapper;

    private StatusVideoDTO statusVideoDTO;
    private Messages expectedMessages;

    @BeforeEach
    void setUp() {
        String validUserId = "123e4567-e89b-12d3-a456-426614174000";
        statusVideoDTO = new StatusVideoDTO();
        statusVideoDTO.setUserId(UUID.fromString(validUserId));
        statusVideoDTO.setVideoId(43L);
        statusVideoDTO.setStatus(StatusType.SUCCESS);

        expectedMessages = new Messages();
        expectedMessages.setRecipient(validUserId);
        expectedMessages.setStatus(StatusType.SUCCESS);
        expectedMessages.setContent("O vídeo com id 43 foi processado com sucesso.");
        expectedMessages.setType(MessageType.P);
    }

    @Test
    void shouldMapStatusVideoDTOToMessagesDomainCorrectly() {
        when(modelMapper.map(statusVideoDTO, Messages.class)).thenReturn(expectedMessages);
        Messages result = statusMessagesMapper.toDomain(statusVideoDTO);
        assertNotNull(result);
        assertEquals(expectedMessages.getRecipient(), result.getRecipient());
        assertEquals(expectedMessages.getStatus(), result.getStatus());
        assertEquals(expectedMessages.getContent(), result.getContent());
        assertEquals(expectedMessages.getType(), result.getType());
        verify(modelMapper, times(1)).map(statusVideoDTO, Messages.class);
    }

    @Test
    void shouldReturnCorrectSuccessMessageForBuildStatusMessage() throws Exception {
        var method = StatusMessagesMapperImpl.class.getDeclaredMethod("buildStatusMessage", StatusVideoDTO.class);
        method.setAccessible(true);
        String message = (String) method.invoke(statusMessagesMapper, statusVideoDTO);
        assertEquals("O vídeo com id 43 foi processado com sucesso.", message);
    }

    @Test
    void shouldReturnCorrectErrorMessageForBuildStatusMessage() throws Exception {
        statusVideoDTO.setStatus(StatusType.ERROR);
        var method = StatusMessagesMapperImpl.class.getDeclaredMethod("buildStatusMessage", StatusVideoDTO.class);
        method.setAccessible(true);
        String message = (String) method.invoke(statusMessagesMapper, statusVideoDTO);
        assertEquals("O vídeo com id 43 foi processado com erro.", message);
    }

    @Test
    void shouldReturnUnknownStatusMessageForBuildStatusMessage() throws Exception {
        statusVideoDTO.setStatus(null);
        var method = StatusMessagesMapperImpl.class.getDeclaredMethod("buildStatusMessage", StatusVideoDTO.class);
        method.setAccessible(true);
        String message = (String) method.invoke(statusMessagesMapper, statusVideoDTO);
        assertEquals("Status desconhecido para o vídeo com id 43.", message);
    }
}
