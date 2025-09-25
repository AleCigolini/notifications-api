package br.com.fiap.messages.application.mapper;

import br.com.fiap.messages.domain.Event;
import br.com.fiap.messages.domain.Messages;

public interface EventMapper {

    Event toSseMessageDTO(Messages messages);
}
