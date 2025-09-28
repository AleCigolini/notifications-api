package br.com.fiap.messages.application.controller;

import br.com.fiap.messages.common.dto.request.StatusVideoDTO;

public interface StatusVideoController {

    void notifyStatusVideo(StatusVideoDTO statusVideoDTO);
}
