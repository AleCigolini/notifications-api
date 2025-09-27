package br.com.fiap.messages.application.gateway;

import br.com.fiap.messages.application.gateway.impl.RedisGatewayImpl;
import br.com.fiap.messages.application.mapper.EventMapper;
import br.com.fiap.messages.common.interfaces.EventRedis;
import br.com.fiap.messages.domain.Event;
import br.com.fiap.messages.domain.Messages;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisGatewayImplTest {

    @Test
    void publishEvent_mapsMessagesToEvent_andCallsEventRedis() {
        EventRedis eventRedis = Mockito.mock(EventRedis.class);
        EventMapper eventMapper = Mockito.mock(EventMapper.class);

        var gateway = new RedisGatewayImpl(eventRedis, eventMapper);

        Messages messages = new Messages();
        Event event = new Event();

        when(eventMapper.toSseMessageDTO(messages)).thenReturn(event);

        gateway.publishEvent(messages);

        verify(eventMapper).toSseMessageDTO(messages);
        verify(eventRedis).publishEvent(event);
    }
}

