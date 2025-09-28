package br.com.fiap.messages.application.mapper.impl;

import br.com.fiap.messages.application.mapper.EventMapper;
import br.com.fiap.messages.domain.Event;
import br.com.fiap.messages.domain.EventPayload;
import br.com.fiap.messages.domain.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@ApplicationScoped
@AllArgsConstructor
public class EventMapperImpl implements EventMapper {

    private ModelMapper mapper;

    public Event toSseMessageDTO(Messages messages) {
        // Mapeamento do payload
        mapper.typeMap(Messages.class, EventPayload.class).addMappings(mapper -> {
            mapper.map(Messages::getContent, EventPayload::setContent);
            mapper.map(Messages::getCreatedAt, EventPayload::setCreatedAt);
            mapper.map(Messages::getStatus, EventPayload::setStatus);
        });

        // Converter para o campo aninhado
        var payloadConverter = (org.modelmapper.Converter<Messages, EventPayload>) ctx ->
                ctx.getSource() == null ? null : mapper.map(ctx.getSource(), EventPayload.class);

        // Mapeamento do DTO principal
        mapper.typeMap(Messages.class, Event.class).addMappings(mapper -> {
            mapper.map(Messages::getId, Event::setId);
            mapper.map(Messages::getType, Event::setType);
            mapper.map(Messages::getRecipient, Event::setRecipient);
            mapper.using(payloadConverter).map(src -> src, Event::setEventPayload);
        });

        return mapper.map(messages, Event.class);
    }
}
