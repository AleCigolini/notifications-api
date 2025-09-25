package br.com.fiap.messages.application.controller;

import br.com.fiap.messages.common.dto.request.MessagesRequestDTO;
import br.com.fiap.messages.common.dto.response.MessagesResponseDTO;
import br.com.fiap.messages.domain.Messages;

public interface MessagesController {

    MessagesResponseDTO save(MessagesRequestDTO messagesRequestDTO);

    MessagesResponseDTO getMessagesByRecipient(String recipient);

}
