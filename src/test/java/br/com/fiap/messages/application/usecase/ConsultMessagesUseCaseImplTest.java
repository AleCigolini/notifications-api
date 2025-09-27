package br.com.fiap.messages.application.usecase;

import br.com.fiap.messages.application.gateway.MessagesGateway;
import br.com.fiap.messages.application.usecase.impl.ConsultMessagesUseCaseImpl;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class ConsultMessagesUseCaseImplTest {

    @Test
    void getMessagesByRecipient_returnsNull_whenGatewayNotUsed() {
        MessagesGateway gateway = Mockito.mock(MessagesGateway.class);
        var useCase = new ConsultMessagesUseCaseImpl(gateway);

        Messages result = useCase.getMessagesByRecipient("any");

        assertThat(result).isNull();
    }
}

