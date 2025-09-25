package br.com.fiap.messages.common.interfaces;

import br.com.fiap.messages.domain.Event;

public interface EventSender {

    void send(Event event);

}
