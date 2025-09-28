package br.com.fiap.messages.common.dto.response;

import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.StatusType;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MessagesResponseDTO {

    private String id;
    private String content;
    private OffsetDateTime created_at;
    private MessageType type;
    private String recipient;
    private StatusType status;

}
