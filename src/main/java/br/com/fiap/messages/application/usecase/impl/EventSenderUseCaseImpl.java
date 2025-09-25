package br.com.fiap.messages.application.usecase.impl;

import br.com.fiap.messages.application.gateway.RedisGateway;
import br.com.fiap.messages.application.usecase.EventSenderUseCase;
import br.com.fiap.messages.domain.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class EventSenderUseCaseImpl implements EventSenderUseCase {

    private final RedisGateway redisGateway;

    @Override
    public void publishEvent(Messages messages) {
        redisGateway.publishEvent(messages);
    }
}