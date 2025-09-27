package br.com.fiap.messages.application.gateway;

import br.com.fiap.messages.application.gateway.impl.MessagesGatewayImpl;
import br.com.fiap.messages.application.mapper.DatabaseMessagesMapper;
import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;
import br.com.fiap.messages.common.interfaces.MessagesDatabase;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class MessagesGatewayImplTest {

    @Test
    void save_mapsToEntity_callsDatabaseAndMapsBackToDomain() {
        MessagesDatabase messagesDatabase = Mockito.mock(MessagesDatabase.class);
        DatabaseMessagesMapper mapper = Mockito.mock(DatabaseMessagesMapper.class);

        var gateway = new MessagesGatewayImpl(messagesDatabase, mapper);

        Messages input = new Messages();
        UUID id = UUID.randomUUID();
        input.setId(id);
        input.setContent("hello-db");

        JpaMessagesEntity entity = new JpaMessagesEntity();
        entity.setId(id);
        entity.setContent("hello-db");

        Messages returnedDomain = new Messages();
        returnedDomain.setId(id);
        returnedDomain.setContent("hello-db");

        when(mapper.toEntity(input)).thenReturn(entity);
        when(messagesDatabase.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(returnedDomain);

        Messages result = gateway.save(input);

        assertThat(result).isSameAs(returnedDomain);
    }

    @Test
    void getMessagesByRecipient_returnsEmptyList_whenNotImplemented() {
        MessagesDatabase messagesDatabase = Mockito.mock(MessagesDatabase.class);
        DatabaseMessagesMapper mapper = Mockito.mock(DatabaseMessagesMapper.class);

        var gateway = new MessagesGatewayImpl(messagesDatabase, mapper);

        List<Messages> result = gateway.getMessagesByRecipient("any");

        assertThat(result).isNotNull().isEmpty();
    }
}

