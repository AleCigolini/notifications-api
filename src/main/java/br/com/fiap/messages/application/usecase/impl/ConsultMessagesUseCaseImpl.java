package br.com.fiap.messages.application.usecase.impl;

import br.com.fiap.messages.application.gateway.MessagesGateway;
import br.com.fiap.messages.application.usecase.ConsultMessagesUseCase;
import br.com.fiap.messages.domain.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class ConsultMessagesUseCaseImpl implements ConsultMessagesUseCase {

    private MessagesGateway messagesGateway;

    @Override
    public Messages getMessagesByRecipient(String recipient) {
        return null;
    }
}
