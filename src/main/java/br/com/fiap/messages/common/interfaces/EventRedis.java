package br.com.fiap.messages.common.interfaces;

import br.com.fiap.messages.domain.Event;

public interface EventRedis {

    void publishEvent(Event event);

    void subscribe();
}
