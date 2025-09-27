package br.com.fiap.messages.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EventPayloadTest {

    @Test
    void testCreateCompleteEventPayload() {
        String content = "Test event content";
        OffsetDateTime createdAt = OffsetDateTime.now();

        EventPayload eventPayload = new EventPayload();
        eventPayload.setContent(content);
        eventPayload.setCreatedAt(createdAt);

        assertEquals(content, eventPayload.getContent());
        assertEquals(createdAt, eventPayload.getCreatedAt());
    }

    @Test
    void testDefaultConstructorWithNullValues() {
        EventPayload eventPayload = new EventPayload();

        assertNull(eventPayload.getContent());
        assertNull(eventPayload.getCreatedAt());
    }

    @Test
    void testUpdateEventPayloadProperties() {
        EventPayload eventPayload = new EventPayload();
        eventPayload.setContent("Initial content");
        OffsetDateTime initialTime = OffsetDateTime.now().minusHours(1);
        eventPayload.setCreatedAt(initialTime);

        String newContent = "Updated content";
        OffsetDateTime newCreatedAt = OffsetDateTime.now();

        eventPayload.setContent(newContent);
        eventPayload.setCreatedAt(newCreatedAt);

        assertEquals(newContent, eventPayload.getContent());
        assertEquals(newCreatedAt, eventPayload.getCreatedAt());
    }
}
