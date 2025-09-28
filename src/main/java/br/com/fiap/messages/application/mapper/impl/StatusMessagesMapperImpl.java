package br.com.fiap.messages.application.mapper.impl;

import br.com.fiap.messages.application.mapper.StatusMessagesMapper;
import br.com.fiap.messages.common.dto.request.StatusVideoDTO;
import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.Messages;
import br.com.fiap.messages.domain.StatusType;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

@ApplicationScoped
@AllArgsConstructor
public class StatusMessagesMapperImpl implements StatusMessagesMapper {

    private ModelMapper mapper;

    @PostConstruct
    public void setupMapper() {
        Converter<StatusVideoDTO, String> messageConverter = ctx ->
                ctx.getSource() == null ? null : buildStatusMessage(ctx.getSource());

        Converter<StatusVideoDTO, MessageType> messageTypeConverter = ctx ->
                ctx.getSource() == null ? null : MessageType.P;

        mapper.createTypeMap(StatusVideoDTO.class, Messages.class)
                .addMappings(mapping -> {
                    mapping.skip(Messages::setId);
                    mapping.map(StatusVideoDTO::getUserId, Messages::setRecipient);
                    mapping.map(StatusVideoDTO::getStatus, Messages::setStatus);
                    mapping.using(messageConverter).map(src -> src, Messages::setContent);
                    mapping.using(messageTypeConverter).map(src -> src, Messages::setType);
                });
    }

    private String buildStatusMessage(StatusVideoDTO dto) {
        if (StatusType.SUCCESS.equals(dto.getStatus())) {
            return "O vídeo com id " + dto.getVideoId() + " foi processado com sucesso.";
        } else if (StatusType.ERROR.equals(dto.getStatus())) {
            return "O vídeo com id " + dto.getVideoId() + " foi processado com erro.";
        }
        return "Status desconhecido para o vídeo com id " + dto.getVideoId() + ".";
    }

    @Override
    public Messages toDomain(StatusVideoDTO statusVideoDTO) {
        return mapper.map(statusVideoDTO, Messages.class);
    }
}
