package br.com.fiap.messages.application.presenter;

import br.com.fiap.messages.common.dto.response.MessagesResponseDTO;
import br.com.fiap.messages.domain.Messages;

public interface MessagesPresenter {
    MessagesResponseDTO toResponse(Messages messages);
}
