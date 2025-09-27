package br.com.fiap.messages.infrastructure.sse.adapter;

import br.com.fiap.messages.domain.Event;
import br.com.fiap.messages.domain.EventPayload;
import br.com.fiap.messages.domain.MessageType;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.subscription.MultiEmitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventSenderAdapterTest {

    private EventSenderAdapter adapter;

    @Mock
    private MultiEmitter<Event> emitter1;

    @Mock
    private MultiEmitter<Event> emitter2;

    @Mock
    private MultiEmitter<Event> emitter3;

    @Captor
    private ArgumentCaptor<Consumer<MultiEmitter<? super Event>>> emitterConsumerCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new EventSenderAdapter();
    }

    @Test
    void subscribeShouldAddClientEmitterToMap() {
        String clientId = "client1";

        Multi<Event> result = adapter.subscribe(clientId);
        AssertSubscriber<Event> subscriber = result.subscribe().withSubscriber(AssertSubscriber.create(10));

        assertNotNull(result);

        Map<String, List<MultiEmitter<? super Event>>> clientsMap = getClientsMap();

        assertTrue(clientsMap.containsKey(clientId));
        assertFalse(clientsMap.get(clientId).isEmpty());
    }

    @Test
    void shouldRemoveEmitterWhenClientTerminatesConnection() throws Exception {
        String clientId = "client1";

        Multi<Event> result = adapter.subscribe(clientId);

        AssertSubscriber<Event> subscriber = result.subscribe().withSubscriber(AssertSubscriber.create(10));

        Map<String, List<MultiEmitter<? super Event>>> clientsMap = getClientsMap();

        assertTrue(clientsMap.containsKey(clientId));
        List<MultiEmitter<? super Event>> emitters = clientsMap.get(clientId);
        assertNotNull(emitters, "Emitters list should not be null");
        assertFalse(emitters.isEmpty(), "Emitters list should not be empty");

        MultiEmitter<? super Event> actualEmitter = emitters.get(0);

        actualEmitter.complete();

        assertFalse(clientsMap.containsKey(clientId), "Client should be removed after disconnection");
    }

    @Test
    void sendShouldBroadcastEventToAllClients() throws Exception {
        setupMockClientsMap("client1", emitter1);
        setupMockClientsMap("client2", emitter2);

        Event broadcastEvent = createEvent(MessageType.B, null, "Test broadcast");

        adapter.send(broadcastEvent);

        verify(emitter1, times(1)).emit(broadcastEvent);
        verify(emitter2, times(1)).emit(broadcastEvent);
    }

    @Test
    void sendShouldDeliverPrivateEventOnlyToRecipient() throws Exception {
        setupMockClientsMap("client1", emitter1);
        setupMockClientsMap("client2", emitter2);

        Event privateEvent = createEvent(MessageType.P, "client1", "Test private message");

        adapter.send(privateEvent);

        verify(emitter1, times(1)).emit(privateEvent);
        verify(emitter2, never()).emit(any());
    }

    @Test
    void sendShouldNotSendWhenEventIsNull() throws Exception {
        setupMockClientsMap("client1", emitter1);

        adapter.send(null);

        verify(emitter1, never()).emit(any());
    }

    @Test
    void sendShouldNotSendWhenPayloadIsNull() throws Exception {
        setupMockClientsMap("client1", emitter1);
        Event eventWithNullPayload = new Event();
        eventWithNullPayload.setType(MessageType.B);

        adapter.send(eventWithNullPayload);

        verify(emitter1, never()).emit(any());
    }

    @Test
    void sendShouldHandleNonExistentRecipient() throws Exception {

        setupMockClientsMap("client1", emitter1);
        Event privateEvent = createEvent(MessageType.P, "non-existent-client", "Message to nobody");

        assertDoesNotThrow(() -> adapter.send(privateEvent));

        verify(emitter1, never()).emit(any());
    }

    @Test
    void shouldHandleMultipleEmittersForSameClient() throws Exception {
        String clientId = "client1";
        setupMockClientsMap(clientId, emitter1);

        Map<String, List<MultiEmitter<? super Event>>> clientsMap = getClientsMap();
        clientsMap.get(clientId).add(emitter2);

        Event privateEvent = createEvent(MessageType.P, clientId, "Test multi-emitter");

        adapter.send(privateEvent);

        verify(emitter1, times(1)).emit(privateEvent);
        verify(emitter2, times(1)).emit(privateEvent);
    }

    @Test
    void shouldMaintainMultipleClientsWithTheirOwnEmitters() throws Exception {
        setupMockClientsMap("client1", emitter1);
        setupMockClientsMap("client2", emitter2);
        setupMockClientsMap("client3", emitter3);

        Map<String, List<MultiEmitter<? super Event>>> clientsMap = getClientsMap();
        assertEquals(3, clientsMap.size());
        assertTrue(clientsMap.containsKey("client1"));
        assertTrue(clientsMap.containsKey("client2"));
        assertTrue(clientsMap.containsKey("client3"));
    }


    private Map<String, List<MultiEmitter<? super Event>>> getClientsMap() {
        try {
            java.lang.reflect.Field clientsField = EventSenderAdapter.class.getDeclaredField("clients");
            clientsField.setAccessible(true);
            return (Map<String, List<MultiEmitter<? super Event>>>) clientsField.get(adapter);
        } catch (Exception e) {
            fail("Failed to access clients field via reflection: " + e.getMessage());
            return null;
        }
    }

    private void setupMockClientsMap(String clientId, MultiEmitter<Event> mockEmitter) {
        Map<String, List<MultiEmitter<? super Event>>> clientsMap = getClientsMap();

        List<MultiEmitter<? super Event>> emitters = new CopyOnWriteArrayList<>();
        emitters.add(mockEmitter);
        clientsMap.put(clientId, emitters);
    }

    private Event createEvent(MessageType type, String recipient, String content) {
        Event event = new Event();
        event.setId("test-id-" + System.currentTimeMillis());
        event.setType(type);
        event.setRecipient(recipient);

        EventPayload payload = new EventPayload();
        payload.setContent(content);
        payload.setCreatedAt(OffsetDateTime.now());
        event.setEventPayload(payload);

        return event;
    }
}
