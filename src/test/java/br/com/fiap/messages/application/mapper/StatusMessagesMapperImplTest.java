package br.com.fiap.messages.application.mapper;

import br.com.fiap.messages.application.mapper.impl.StatusMessagesMapperImpl;
import br.com.fiap.messages.common.dto.request.StatusVideoDTO;
import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.Messages;
import br.com.fiap.messages.domain.StatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StatusMessagesMapperImplTest {

    private ModelMapper modelMapper;
    private StatusMessagesMapperImpl statusMessagesMapper;

    private StatusVideoDTO statusVideoDTO;
    private Messages expectedMessages;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        statusMessagesMapper = new StatusMessagesMapperImpl(modelMapper);
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
        Messages result = statusMessagesMapper.toDomain(statusVideoDTO);
        assertNotNull(result);
        assertEquals(expectedMessages.getRecipient(), result.getRecipient());
        assertEquals(expectedMessages.getStatus(), result.getStatus());
        assertEquals(expectedMessages.getContent(), result.getContent());
        assertEquals(expectedMessages.getType(), result.getType());
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
