package br.com.fiap.messages.common.dto.request;

import br.com.fiap.messages.domain.StatusType;
import lombok.Data;

import java.util.UUID;

@Data
public class StatusVideoDTO {

    private UUID userId;
    private Long videoId;
    private StatusType status;

}
