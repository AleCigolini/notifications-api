package br.com.fiap.messages.application.mapper.impl;

import br.com.fiap.messages.application.mapper.RequestMessagesMapper;
import br.com.fiap.messages.common.dto.request.MessagesRequestDTO;
import br.com.fiap.messages.domain.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@ApplicationScoped
@AllArgsConstructor
public class RequestMessagesMapperImpl implements RequestMessagesMapper {

    private ModelMapper mapper;

    @Override
    public Messages toDomain(MessagesRequestDTO messagesRequestDTO) {
        return mapper.map(messagesRequestDTO, Messages.class);
    }
}
