package br.com.fiap.messages.application.controller;

import br.com.fiap.messages.application.controller.impl.MessagesControllerImpl;
import br.com.fiap.messages.application.mapper.DatabaseMessagesMapper;
import br.com.fiap.messages.application.mapper.EventMapper;
import br.com.fiap.messages.application.mapper.RequestMessagesMapper;
import br.com.fiap.messages.application.presenter.MessagesPresenter;
import br.com.fiap.messages.common.dto.request.MessagesRequestDTO;
import br.com.fiap.messages.common.dto.response.MessagesResponseDTO;
import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;
import br.com.fiap.messages.common.interfaces.EventRedis;
import br.com.fiap.messages.common.interfaces.MessagesDatabase;
import br.com.fiap.messages.domain.Event;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MessagesControllerImplTest {

    @Test
    void save_shouldMapRequest_saveToDatabase_publishEvent_andReturnResponse() {
        MessagesDatabase messagesDatabase = Mockito.mock(MessagesDatabase.class);
        DatabaseMessagesMapper databaseMapper = Mockito.mock(DatabaseMessagesMapper.class);
        RequestMessagesMapper requestMapper = Mockito.mock(RequestMessagesMapper.class);
        MessagesPresenter presenter = Mockito.mock(MessagesPresenter.class);
        EventRedis eventRedis = Mockito.mock(EventRedis.class);
        EventMapper eventMapper = Mockito.mock(EventMapper.class);

        var controller = new MessagesControllerImpl(messagesDatabase, databaseMapper, requestMapper, presenter, eventRedis, eventMapper);

        MessagesRequestDTO requestDTO = new MessagesRequestDTO();
        requestDTO.setContent("hello");
        requestDTO.setRecipient("r1");

        Messages domainInput = new Messages();
        domainInput.setContent("hello");

        JpaMessagesEntity entity = new JpaMessagesEntity();
        entity.setContent("hello");

        JpaMessagesEntity savedEntity = new JpaMessagesEntity();
        savedEntity.setContent("hello");

        Messages savedDomain = new Messages();
        savedDomain.setContent("hello");

        Event event = new Event();

        MessagesResponseDTO responseDTO = new MessagesResponseDTO();
        responseDTO.setContent("hello");

        when(requestMapper.toDomain(requestDTO)).thenReturn(domainInput);
        when(databaseMapper.toEntity(domainInput)).thenReturn(entity);
        when(messagesDatabase.save(entity)).thenReturn(savedEntity);
        when(databaseMapper.toDomain(savedEntity)).thenReturn(savedDomain);
        when(eventMapper.toSseMessageDTO(savedDomain)).thenReturn(event);
        when(presenter.toResponse(savedDomain)).thenReturn(responseDTO);

        MessagesResponseDTO result = controller.save(requestDTO);

        assertThat(result).isSameAs(responseDTO);

        verify(requestMapper).toDomain(requestDTO);
        verify(databaseMapper).toEntity(domainInput);
        verify(messagesDatabase).save(entity);
        verify(databaseMapper).toDomain(savedEntity);
        verify(eventMapper).toSseMessageDTO(savedDomain);
        verify(eventRedis).publishEvent(event);
        verify(presenter).toResponse(savedDomain);
    }

    @Test
    void save_whenRequestMapperReturnsNull_thenReturnsNullAndCallsDownstreamWithNull() {
        MessagesDatabase messagesDatabase = Mockito.mock(MessagesDatabase.class);
        DatabaseMessagesMapper databaseMapper = Mockito.mock(DatabaseMessagesMapper.class);
        RequestMessagesMapper requestMapper = Mockito.mock(RequestMessagesMapper.class);
        MessagesPresenter presenter = Mockito.mock(MessagesPresenter.class);
        EventRedis eventRedis = Mockito.mock(EventRedis.class);
        EventMapper eventMapper = Mockito.mock(EventMapper.class);

        var controller = new MessagesControllerImpl(messagesDatabase, databaseMapper, requestMapper, presenter, eventRedis, eventMapper);

        MessagesRequestDTO requestDTO = new MessagesRequestDTO();

        when(requestMapper.toDomain(requestDTO)).thenReturn(null);

        MessagesResponseDTO result = controller.save(requestDTO);

        assertThat(result).isNull();

        verify(requestMapper).toDomain(requestDTO);
        verify(databaseMapper).toEntity(null);
        verify(messagesDatabase).save(isNull());
        verify(databaseMapper).toDomain(isNull());
        verify(eventMapper).toSseMessageDTO(null);
        verify(eventRedis).publishEvent(isNull());
        verify(presenter).toResponse(isNull());
    }
}
