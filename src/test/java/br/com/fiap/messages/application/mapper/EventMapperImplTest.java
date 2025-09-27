package br.com.fiap.messages.application.mapper;

import br.com.fiap.messages.application.mapper.impl.EventMapperImpl;
import br.com.fiap.messages.domain.Event;
import br.com.fiap.messages.domain.EventPayload;
import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EventMapperImplTest {

    @Test
    void toSseMessageDTO_mapsMessagesToEvent_withNestedPayload() {
        ModelMapper modelMapper = new ModelMapper();
        var mapper = new EventMapperImpl(modelMapper);

        Messages messages = new Messages();
        UUID id = UUID.randomUUID();
        messages.setId(id);
        messages.setContent("payload content");
        messages.setCreatedAt(OffsetDateTime.parse("2020-01-01T10:00:00Z"));
        messages.setRecipient("recipient-1");
        messages.setType(MessageType.B);

        Event event = mapper.toSseMessageDTO(messages);

        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo(id.toString());
        assertThat(event.getRecipient()).isEqualTo("recipient-1");
        assertThat(event.getType()).isEqualTo(MessageType.B);

        EventPayload payload = event.getEventPayload();
        assertThat(payload).isNotNull();
        assertThat(payload.getContent()).isEqualTo("payload content");
        assertThat(payload.getCreatedAt()).isEqualTo(OffsetDateTime.parse("2020-01-01T10:00:00Z"));
    }
}
