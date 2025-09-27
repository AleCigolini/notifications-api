package br.com.fiap.messages.application.usecase;

import br.com.fiap.messages.application.gateway.RedisGateway;
import br.com.fiap.messages.application.usecase.impl.EventSenderUseCaseImpl;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

class EventSenderUseCaseImplTest {

    @Test
    void publishEvent_delegatesToRedisGateway() {
        RedisGateway redisGateway = Mockito.mock(RedisGateway.class);
        var useCase = new EventSenderUseCaseImpl(redisGateway);

        Messages messages = new Messages();
        useCase.publishEvent(messages);

        verify(redisGateway).publishEvent(messages);
    }
}

