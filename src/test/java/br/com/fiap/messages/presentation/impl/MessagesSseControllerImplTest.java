package br.com.fiap.messages.presentation.impl;

import br.com.fiap.messages.domain.Event;
import br.com.fiap.messages.infrastructure.sse.adapter.EventSenderAdapter;
import io.smallrye.mutiny.Multi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the MessagesSseControllerImpl class
 * Tests the server-sent events (SSE) controller behavior
 */
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

        Multi<Event> result = controller.streamMessages(clientId);

        assertNotNull(result, "The returned Multi should not be null");
        verify(adapter).subscribe(clientId);
        assertEquals(mockMulti, result, "The controller should return the Multi from the adapter");
    }

    @Test
    void shouldHandleSubscriptionForNullClientId() {
        String nullClientId = null;
        when(adapter.subscribe(nullClientId)).thenReturn(mockMulti);

        Multi<Event> result = controller.streamMessages(nullClientId);

        assertNotNull(result, "The returned Multi should not be null even with null clientId");
        verify(adapter).subscribe(nullClientId);
        assertEquals(mockMulti, result, "The controller should return the Multi from the adapter");
    }

    @Test
    void shouldHandleSubscriptionForEmptyClientId() {
        String emptyClientId = "";
        when(adapter.subscribe(emptyClientId)).thenReturn(mockMulti);

        Multi<Event> result = controller.streamMessages(emptyClientId);

        assertNotNull(result, "The returned Multi should not be null even with empty clientId");
        verify(adapter).subscribe(emptyClientId);
        assertEquals(mockMulti, result, "The controller should return the Multi from the adapter");
    }
}
