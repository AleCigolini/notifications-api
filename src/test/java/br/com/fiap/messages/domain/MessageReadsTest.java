package br.com.fiap.messages.domain;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MessageReadsTest {

    @Test
    void shouldCreateMessageReadsWithAllProperties() {

        MessageReads messageReads = new MessageReads();
        UUID id = UUID.randomUUID();
        Messages message = new Messages();
        String userId = "user123";
        OffsetDateTime readAt = OffsetDateTime.now();

        messageReads.setId(id);
        messageReads.setMessage(message);
        messageReads.setUser_id(userId);
        messageReads.setRead_at(readAt);

        assertEquals(id, messageReads.getId());
        assertEquals(message, messageReads.getMessage());
        assertEquals(userId, messageReads.getUser_id());
        assertEquals(readAt, messageReads.getRead_at());
    }

    @Test
    void shouldSetAndGetId() {

        MessageReads messageReads = new MessageReads();
        UUID id = UUID.randomUUID();

        messageReads.setId(id);

        assertEquals(id, messageReads.getId());
    }

    @Test
    void shouldSetAndGetMessage() {
        MessageReads messageReads = new MessageReads();
        Messages message = new Messages();
        UUID messageId = UUID.randomUUID();
        message.setId(messageId);

        messageReads.setMessage(message);

        assertEquals(message, messageReads.getMessage());
        assertEquals(messageId, messageReads.getMessage().getId());
    }

    @Test
    void shouldSetAndGetUserId() {
        MessageReads messageReads = new MessageReads();
        String userId = "user123";

        messageReads.setUser_id(userId);

        assertEquals(userId, messageReads.getUser_id());
    }

    @Test
    void shouldSetAndGetReadAt() {
        MessageReads messageReads = new MessageReads();
        OffsetDateTime readAt = OffsetDateTime.of(2023, 5, 15, 10, 30, 0, 0, ZoneOffset.UTC);

        messageReads.setRead_at(readAt);

        assertEquals(readAt, messageReads.getRead_at());
    }

    @Test
    void shouldHandleNullValues() {
        MessageReads messageReads = new MessageReads();

        assertNull(messageReads.getId());
        assertNull(messageReads.getMessage());
        assertNull(messageReads.getUser_id());
        assertNull(messageReads.getRead_at());

        messageReads.setId(null);
        messageReads.setMessage(null);
        messageReads.setUser_id(null);
        messageReads.setRead_at(null);

        assertNull(messageReads.getId());
        assertNull(messageReads.getMessage());
        assertNull(messageReads.getUser_id());
        assertNull(messageReads.getRead_at());
    }
}
