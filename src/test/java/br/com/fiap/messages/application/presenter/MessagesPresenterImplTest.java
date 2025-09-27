package br.com.fiap.messages.application.presenter;

import br.com.fiap.messages.application.presenter.impl.MessagesPresenterImpl;
import br.com.fiap.messages.common.dto.response.MessagesResponseDTO;
import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MessagesPresenterImplTest {

    private ModelMapper modelMapper;
    private MessagesPresenterImpl presenter;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        presenter = new MessagesPresenterImpl(modelMapper);
    }

    @Test
    void toResponse_shouldMapPrivateType_whenDomainTypeIsP() {

        Messages domain = new Messages();
        domain.setId(UUID.randomUUID());
        domain.setType(MessageType.P);

        MessagesResponseDTO dto = presenter.toResponse(domain);

        assertThat(dto).isNotNull();
        assertThat(dto.getType()).isEqualTo(MessageType.P);
    }

}
