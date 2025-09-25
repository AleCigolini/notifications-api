package br.com.fiap.messages.presentation;

import br.com.fiap.messages.common.dto.request.MessagesRequestDTO;
import br.com.fiap.messages.common.dto.response.MessagesResponseDTO;

public interface MessagesRestController {

    MessagesResponseDTO save(MessagesRequestDTO messagesRequestDTO);

}
