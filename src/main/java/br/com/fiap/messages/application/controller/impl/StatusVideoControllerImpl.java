package br.com.fiap.messages.application.controller.impl;

import br.com.fiap.messages.application.controller.StatusVideoController;
import br.com.fiap.messages.application.gateway.MessagesGateway;
import br.com.fiap.messages.application.gateway.RedisGateway;
import br.com.fiap.messages.application.gateway.impl.MessagesGatewayImpl;
import br.com.fiap.messages.application.gateway.impl.RedisGatewayImpl;
import br.com.fiap.messages.application.mapper.DatabaseMessagesMapper;
import br.com.fiap.messages.application.mapper.EventMapper;
import br.com.fiap.messages.application.mapper.StatusMessagesMapper;
import br.com.fiap.messages.application.usecase.EventSenderUseCase;
import br.com.fiap.messages.application.usecase.SaveMessagesUseCase;
import br.com.fiap.messages.application.usecase.impl.EventSenderUseCaseImpl;
import br.com.fiap.messages.application.usecase.impl.SaveMessagesUseCaseImpl;
import br.com.fiap.messages.common.dto.request.StatusVideoDTO;
import br.com.fiap.messages.common.interfaces.EventRedis;
import br.com.fiap.messages.common.interfaces.MessagesDatabase;
import br.com.fiap.messages.domain.Messages;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StatusVideoControllerImpl implements StatusVideoController {

    private final SaveMessagesUseCase saveMessagesUseCase;
    private final StatusMessagesMapper statusMessagesMapper;
    private final EventSenderUseCase eventSenderUseCase;

    public StatusVideoControllerImpl(MessagesDatabase messagesDatabase,
                                     DatabaseMessagesMapper databaseMessagesMapper,
                                     StatusMessagesMapper statusMessagesMapper,
                                     EventRedis eventRedis,
                                     EventMapper eventMapper
    ) {
        this.statusMessagesMapper = statusMessagesMapper;
        final MessagesGateway messagesGateway = new MessagesGatewayImpl(messagesDatabase, databaseMessagesMapper);
        final RedisGateway redisGateway = new RedisGatewayImpl(eventRedis, eventMapper);
        this.saveMessagesUseCase = new SaveMessagesUseCaseImpl(messagesGateway);
        this.eventSenderUseCase = new EventSenderUseCaseImpl(redisGateway);
    }

    @Override
    public void notifyStatusVideo(StatusVideoDTO statusVideoDTO) {

        Messages messages = statusMessagesMapper.toDomain(statusVideoDTO);

        messages = saveMessagesUseCase.save(messages);
        eventSenderUseCase.publishEvent(messages);
    }
}
