package br.com.fiap.messages.application.usecase;

import br.com.fiap.messages.domain.Messages;

public interface SaveMessagesUseCase {

    Messages save(Messages messages);
}
