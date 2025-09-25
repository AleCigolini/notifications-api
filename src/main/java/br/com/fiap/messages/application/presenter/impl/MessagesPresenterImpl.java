package br.com.fiap.messages.application.presenter.impl;

import br.com.fiap.messages.application.presenter.MessagesPresenter;
import br.com.fiap.messages.common.dto.response.MessagesResponseDTO;
import br.com.fiap.messages.domain.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@ApplicationScoped
@AllArgsConstructor
public class MessagesPresenterImpl implements MessagesPresenter {

    private ModelMapper modelMapper;

    @Override
    public MessagesResponseDTO toResponse(Messages messages) {
        return modelMapper.map(messages, MessagesResponseDTO.class);
    }
}
