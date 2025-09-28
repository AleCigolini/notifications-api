package br.com.fiap.messages.presentation;

import br.com.fiap.messages.common.dto.request.StatusVideoDTO;

public interface VideoStatusConsumer {

    void videoStatusConsume(StatusVideoDTO statusVideoDTO);
}
