package br.com.fiap.messages.application.usecase;

import br.com.fiap.messages.application.usecase.impl.SaveMessagesUseCaseImpl;
import br.com.fiap.messages.application.gateway.MessagesGateway;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SaveMessagesUseCaseImplTest {

    @Test
    void save_delegatesToGateway_andReturnsSaved() {
        MessagesGateway gateway = Mockito.mock(MessagesGateway.class);
        var useCase = new SaveMessagesUseCaseImpl(gateway);

        Messages input = new Messages();
        input.setContent("hi");

        Messages saved = new Messages();
        saved.setContent("hi");

        when(gateway.save(input)).thenReturn(saved);

        Messages result = useCase.save(input);

        assertThat(result).isSameAs(saved);
    }
}

