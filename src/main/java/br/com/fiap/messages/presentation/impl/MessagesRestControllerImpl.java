package br.com.fiap.messages.presentation.impl;

import br.com.fiap.messages.application.controller.MessagesController;
import br.com.fiap.messages.common.dto.request.MessagesRequestDTO;
import br.com.fiap.messages.common.dto.response.MessagesResponseDTO;
import br.com.fiap.messages.presentation.MessagesRestController;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MessagesRestControllerImpl implements MessagesRestController {

    private final MessagesController messagesController;


    @POST
    public MessagesResponseDTO save(MessagesRequestDTO messagesRequestDTO) {

        return messagesController.save(messagesRequestDTO);
    }
}