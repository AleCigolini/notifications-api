package br.com.fiap.messages.application.mapper;

import br.com.fiap.messages.application.mapper.impl.DatabaseMessagesMapperImpl;
import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;
import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseMessagesMapperImplTest {

    @Test
    void toEntity_and_toDomain_roundtrip_fieldsPreserved() {
        ModelMapper modelMapper = new ModelMapper();
        var mapper = new DatabaseMessagesMapperImpl(modelMapper);

        Messages domain = new Messages();
        UUID id = UUID.randomUUID();
        domain.setId(id);
        domain.setContent("db content");
        domain.setCreatedAt(OffsetDateTime.parse("2021-02-02T12:00:00Z"));
        domain.setType(MessageType.B);
        domain.setRecipient("rcp");
        domain.setChannel("SMS");

        JpaMessagesEntity entity = mapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getContent()).isEqualTo("db content");
        assertThat(entity.getType()).isEqualTo(MessageType.B);
        assertThat(entity.getRecipient()).isEqualTo("rcp");
        assertThat(entity.getChannel()).isEqualTo("SMS");

        Messages mappedBack = mapper.toDomain(entity);

        assertThat(mappedBack).isNotNull();
        assertThat(mappedBack.getId()).isEqualTo(id);
        assertThat(mappedBack.getContent()).isEqualTo("db content");
        assertThat(mappedBack.getType()).isEqualTo(MessageType.B);
        assertThat(mappedBack.getRecipient()).isEqualTo("rcp");
        assertThat(mappedBack.getChannel()).isEqualTo("SMS");
    }
}
