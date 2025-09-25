package br.com.fiap.messages.application.controller.impl;

import br.com.fiap.messages.application.controller.MessagesController;
import br.com.fiap.messages.application.gateway.MessagesGateway;
import br.com.fiap.messages.application.gateway.RedisGateway;
import br.com.fiap.messages.application.gateway.impl.MessagesGatewayImpl;
import br.com.fiap.messages.application.gateway.impl.RedisGatewayImpl;
import br.com.fiap.messages.application.mapper.DatabaseMessagesMapper;
import br.com.fiap.messages.application.mapper.EventMapper;
import br.com.fiap.messages.application.mapper.RequestMessagesMapper;
import br.com.fiap.messages.application.presenter.MessagesPresenter;
import br.com.fiap.messages.application.usecase.ConsultMessagesUseCase;
import br.com.fiap.messages.application.usecase.EventSenderUseCase;
import br.com.fiap.messages.application.usecase.SaveMessagesUseCase;
import br.com.fiap.messages.application.usecase.impl.ConsultMessagesUseCaseImpl;
import br.com.fiap.messages.application.usecase.impl.EventSenderUseCaseImpl;
import br.com.fiap.messages.application.usecase.impl.SaveMessagesUseCaseImpl;
import br.com.fiap.messages.common.dto.request.MessagesRequestDTO;
import br.com.fiap.messages.common.dto.response.MessagesResponseDTO;
import br.com.fiap.messages.common.interfaces.EventRedis;
import br.com.fiap.messages.common.interfaces.MessagesDatabase;
import br.com.fiap.messages.domain.Messages;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessagesControllerImpl implements MessagesController {

    private final SaveMessagesUseCase saveMessagesUseCase;
    private final ConsultMessagesUseCase consultMessagesUseCase;

    private final RequestMessagesMapper requestMessagesMapper;
    private final MessagesPresenter messagesPresenter;

    private final EventSenderUseCase eventSenderUseCase;

    public MessagesControllerImpl(MessagesDatabase messagesDatabase,
                                  DatabaseMessagesMapper databaseMessagesMapper,
                                  RequestMessagesMapper requestMessagesMapper,
                                  MessagesPresenter messagesPresenter,
                                  EventRedis eventRedis,
                                  EventMapper eventMapper
    ) {
        this.requestMessagesMapper = requestMessagesMapper;
        this.messagesPresenter = messagesPresenter;

        final MessagesGateway messagesGateway = new MessagesGatewayImpl(messagesDatabase, databaseMessagesMapper);
        final RedisGateway redisGateway = new RedisGatewayImpl(eventRedis, eventMapper);
        this.saveMessagesUseCase = new SaveMessagesUseCaseImpl(messagesGateway);
        this.consultMessagesUseCase = new ConsultMessagesUseCaseImpl(messagesGateway);
        this.eventSenderUseCase = new EventSenderUseCaseImpl(redisGateway);
    }

    @Override
    public MessagesResponseDTO save(MessagesRequestDTO messagesRequestDTO) {

        Messages messages = saveMessagesUseCase.save(requestMessagesMapper.toDomain(messagesRequestDTO));
        eventSenderUseCase.publishEvent(messages);

        return messagesPresenter.toResponse(messages);
    }

    @Override
    public MessagesResponseDTO getMessagesByRecipient(String recipient) {
        return null;
    }
}
