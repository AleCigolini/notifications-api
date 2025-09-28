package br.com.fiap.messages.presentation.impl;

import br.com.fiap.messages.application.controller.StatusVideoController;
import br.com.fiap.messages.common.dto.request.StatusVideoDTO;
import br.com.fiap.messages.presentation.VideoStatusConsumer;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class VideoStatusConsumerImpl implements VideoStatusConsumer {

    private static final Logger log = LoggerFactory.getLogger(VideoStatusConsumerImpl.class);

    private final StatusVideoController statusVideoController;

    @Override
    @Incoming("video-status")
    public void videoStatusConsume(StatusVideoDTO statusVideoDTO) {

        String correlationId = UUID.randomUUID().toString();

        log.info("[{}] Iniciando processamento da mensagem recebida do Kafka: {}", correlationId, statusVideoDTO);

        try {
            statusVideoController.notifyStatusVideo(statusVideoDTO);
            log.info("[{}] Processamento concluído e mensagem enviada para o cliente: {}", correlationId, statusVideoDTO);
        } catch (Exception e) {
            log.error("[{}] Erro ao processar mensagem: {}", correlationId, statusVideoDTO, e);
            throw e;
        }
    }
}
