package br.com.fiap.messages.application.usecase.impl;

import br.com.fiap.messages.application.gateway.MessagesGateway;
import br.com.fiap.messages.application.usecase.SaveMessagesUseCase;
import br.com.fiap.messages.domain.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

@ApplicationScoped
@AllArgsConstructor
public class SaveMessagesUseCaseImpl implements SaveMessagesUseCase {

    private MessagesGateway messagesGateway;

    @Override
    public Messages save(Messages messages) {
        return messagesGateway.save(messages);
    }
}
