package br.com.fiap.messages.application.mapper;

import br.com.fiap.messages.common.dto.request.MessagesRequestDTO;
import br.com.fiap.messages.domain.Messages;

public interface RequestMessagesMapper {

    Messages toDomain(MessagesRequestDTO messagesRequestDTO);
}
