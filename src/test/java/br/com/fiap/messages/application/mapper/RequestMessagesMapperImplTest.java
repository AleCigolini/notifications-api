package br.com.fiap.messages.application.mapper;

import br.com.fiap.messages.application.mapper.impl.RequestMessagesMapperImpl;
import br.com.fiap.messages.common.dto.request.MessagesRequestDTO;
import br.com.fiap.messages.domain.Messages;
import br.com.fiap.messages.domain.StatusType;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

class RequestMessagesMapperImplTest {

    @Test
    void toDomain_mapsFields() {
        ModelMapper modelMapper = new ModelMapper();
        var mapper = new RequestMessagesMapperImpl(modelMapper);

        var dto = new MessagesRequestDTO();
        dto.setContent("hello");
        dto.setRecipient("user-1");
        dto.setStatus(StatusType.INFO);

        Messages domain = mapper.toDomain(dto);

        assertThat(domain).isNotNull();
        assertThat(domain.getContent()).isEqualTo("hello");
        assertThat(domain.getRecipient()).isEqualTo("user-1");
        assertThat(domain.getStatus()).isEqualTo(StatusType.INFO);
    }
}

