package br.com.fiap.messages.presentation.impl;

import br.com.fiap.messages.domain.Event;
import br.com.fiap.messages.infrastructure.sse.adapter.EventSenderAdapter;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

@Path("/sse/messages")
@RequiredArgsConstructor
public class MessagesSseControllerImpl {

    @Inject
    EventSenderAdapter adapter;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<Event> streamMessages(@QueryParam("clientId") String clientId) {
        return adapter.subscribe(clientId);
    }
}