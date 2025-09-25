package br.com.fiap.messages.application.gateway.impl;

import br.com.fiap.messages.application.gateway.MessagesGateway;
import br.com.fiap.messages.application.mapper.DatabaseMessagesMapper;
import br.com.fiap.messages.common.interfaces.MessagesDatabase;
import br.com.fiap.messages.domain.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
public class MessagesGatewayImpl implements MessagesGateway {

    private MessagesDatabase messagesDatabase;
    private DatabaseMessagesMapper mapper;

    @Override
    public Messages save(Messages messages) {

        final var messagesEntity = mapper.toEntity(messages);

        return mapper.toDomain(messagesDatabase.save(messagesEntity));
    }

    @Override
    public List<Messages> getMessagesByRecipient(String recipient) {
        return List.of();
    }
}
