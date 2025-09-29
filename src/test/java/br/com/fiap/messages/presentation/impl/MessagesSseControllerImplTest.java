package br.com.fiap.messages.presentation.impl;

import br.com.fiap.messages.domain.Event;
import br.com.fiap.messages.infrastructure.sse.adapter.EventSenderAdapter;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessagesSseControllerImplTest {

    @Mock
    private EventSenderAdapter adapter;

    @InjectMocks
    private MessagesSseControllerImpl controller;

    private final String clientId = "test-client-123";
    private Multi<Event> mockMulti;

    @BeforeEach
    void setUp() {
        mockMulti = Multi.createFrom().empty();
    }

    @Test
    void shouldSubscribeClientToEventStream() {
        when(adapter.subscribe(clientId)).thenReturn(mockMulti);

        Response response = controller.streamMessages(clientId);

        assertNotNull(response, "The returned Response should not be null");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(), "Status should be 200 OK");
        assertEquals("https://storagefiapdev.z15.web.core.windows.net", response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("true", response.getHeaderString("Access-Control-Allow-Credentials"));
        assertEquals("no-cache", response.getHeaderString("Cache-Control"));
        assertEquals("no", response.getHeaderString("X-Accel-Buffering"));
        assertEquals("keep-alive", response.getHeaderString("Connection"));
        verify(adapter).subscribe(clientId);
    }

    @Test
    void shouldHandleSubscriptionForNullClientId() {
        String nullClientId = null;
        when(adapter.subscribe(nullClientId)).thenReturn(mockMulti);

        Response response = controller.streamMessages(nullClientId);

        assertNotNull(response, "The returned Response should not be null even with null clientId");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(), "Status should be 200 OK");
        verify(adapter).subscribe(nullClientId);
    }

    @Test
    void shouldHandleSubscriptionForEmptyClientId() {
        String emptyClientId = "";
        when(adapter.subscribe(emptyClientId)).thenReturn(mockMulti);

        Response response = controller.streamMessages(emptyClientId);

        assertNotNull(response, "The returned Response should not be null even with empty clientId");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(), "Status should be 200 OK");
        verify(adapter).subscribe(emptyClientId);
    }
}
