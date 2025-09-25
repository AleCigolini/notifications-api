package br.com.fiap.messages.application.gateway;

import br.com.fiap.messages.domain.Messages;

public interface RedisGateway {

    void publishEvent(Messages messages);
}
