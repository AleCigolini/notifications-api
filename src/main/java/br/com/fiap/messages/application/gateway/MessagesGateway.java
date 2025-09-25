package br.com.fiap.messages.application.gateway;

import br.com.fiap.messages.domain.Messages;

import java.util.List;

public interface MessagesGateway {

    Messages save(Messages messages);
    List<Messages> getMessagesByRecipient(String recipient);
}
