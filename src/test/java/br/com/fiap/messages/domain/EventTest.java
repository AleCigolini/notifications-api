package br.com.fiap.messages.domain;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EventTest {

    @Test
    void testCreateCompleteEvent() {
        String id = "event-123";
        MessageType type = MessageType.P;
        String recipient = "user123";
        EventPayload eventPayload = new EventPayload();
        eventPayload.setContent("Test content");
        eventPayload.setCreatedAt(OffsetDateTime.now());
        eventPayload.setStatus(StatusType.SUCCESS);

        Event event = new Event();
        event.setId(id);
        event.setType(type);
        event.setRecipient(recipient);
        event.setEventPayload(eventPayload);

        assertEquals(id, event.getId());
        assertEquals(type, event.getType());
        assertEquals(recipient, event.getRecipient());
        assertEquals(eventPayload, event.getEventPayload());
    }

    @Test
    void testDefaultConstructorWithNullValues() {
        Event event = new Event();

        assertNull(event.getId());
        assertNull(event.getType());
        assertNull(event.getRecipient());
        assertNull(event.getEventPayload());
    }

    @Test
    void testUpdateEventProperties() {
        Event event = new Event();
        event.setId("initial-id");
        event.setType(MessageType.B);

        String newId = "updated-id";
        MessageType newType = MessageType.P;
        String newRecipient = "newuser456";
        EventPayload newPayload = new EventPayload();
        newPayload.setContent("New content");
        OffsetDateTime now = OffsetDateTime.now();
        newPayload.setCreatedAt(now);
        newPayload.setStatus(StatusType.SUCCESS);

        event.setId(newId);
        event.setType(newType);
        event.setRecipient(newRecipient);
        event.setEventPayload(newPayload);

        assertEquals(newId, event.getId());
        assertEquals(newType, event.getType());
        assertEquals(newRecipient, event.getRecipient());
        assertEquals(newPayload, event.getEventPayload());
        assertEquals("New content", event.getEventPayload().getContent());
        assertEquals(now, event.getEventPayload().getCreatedAt());
        assertEquals(StatusType.SUCCESS, event.getEventPayload().getStatus());
    }
}
