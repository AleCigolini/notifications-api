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
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@Path("/sse/messages")
@RequiredArgsConstructor
public class MessagesSseControllerImpl {

    @Inject
    EventSenderAdapter adapter;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Response streamMessages(@QueryParam("clientId") String clientId) {
        Multi<Event> stream = adapter.subscribe(clientId);
        return Response.ok(stream)
                .header("Access-Control-Allow-Origin", "https://storagefiapdev.z15.web.core.windows.net")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Cache-Control", "no-cache")
                .header("X-Accel-Buffering", "no")
                .header("Connection", "keep-alive")
                .build();
    }
}