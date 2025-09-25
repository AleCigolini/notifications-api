package br.com.fiap.messages.application.gateway.impl;

import br.com.fiap.messages.application.gateway.RedisGateway;
import br.com.fiap.messages.application.mapper.EventMapper;
import br.com.fiap.messages.common.interfaces.EventRedis;
import br.com.fiap.messages.domain.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class RedisGatewayImpl implements RedisGateway {

    private EventRedis eventRedis;
    private EventMapper eventMapper;

    @Override
    public void publishEvent(Messages messages) {
        eventRedis.publishEvent(eventMapper.toSseMessageDTO(messages));
    }

}