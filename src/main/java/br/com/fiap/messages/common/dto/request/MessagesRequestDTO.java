package br.com.fiap.messages.common.dto.request;

import br.com.fiap.messages.domain.MessageType;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MessagesRequestDTO {

    private String content;
    private MessageType type;
    private String recipient;
    private String channel;

}
