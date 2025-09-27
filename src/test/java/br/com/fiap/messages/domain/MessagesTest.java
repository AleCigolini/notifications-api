package br.com.fiap.messages.domain;

import br.com.fiap.messages.common.domain.entity.JpaMessageReadsEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MessagesTest {

    @Test
    void testCreateCompleteMessage() {
        UUID id = UUID.randomUUID();
        String content = "Test message content";
        OffsetDateTime createdAt = OffsetDateTime.now();
        MessageType type = MessageType.P;
        String recipient = "user123";
        String channel = "email";
        Set<JpaMessageReadsEntity> messageReads = new HashSet<>();

        Messages messages = new Messages();
        messages.setId(id);
        messages.setContent(content);
        messages.setCreatedAt(createdAt);
        messages.setType(type);
        messages.setRecipient(recipient);
        messages.setChannel(channel);
        messages.setMessageReads(messageReads);

        assertEquals(id, messages.getId());
        assertEquals(content, messages.getContent());
        assertEquals(createdAt, messages.getCreatedAt());
        assertEquals(type, messages.getType());
        assertEquals(recipient, messages.getRecipient());
        assertEquals(channel, messages.getChannel());
        assertEquals(messageReads, messages.getMessageReads());
    }

    @Test
    void testDefaultConstructorWithNullValues() {
        Messages messages = new Messages();

        assertNull(messages.getId());
        assertNull(messages.getContent());
        assertNull(messages.getCreatedAt());
        assertNull(messages.getType());
        assertNull(messages.getRecipient());
        assertNull(messages.getChannel());
        assertNull(messages.getMessageReads());
    }

    @Test
    void testUpdateMessageProperties() {

        Messages messages = new Messages();
        UUID initialId = UUID.randomUUID();
        messages.setId(initialId);
        messages.setContent("Initial content");
        messages.setType(MessageType.B);

        UUID newId = UUID.randomUUID();
        String newContent = "Updated content";
        OffsetDateTime newCreatedAt = OffsetDateTime.now();
        MessageType newType = MessageType.P;
        String newRecipient = "newuser456";
        String newChannel = "push";
        Set<JpaMessageReadsEntity> newMessageReads = new HashSet<>();

        messages.setId(newId);
        messages.setContent(newContent);
        messages.setCreatedAt(newCreatedAt);
        messages.setType(newType);
        messages.setRecipient(newRecipient);
        messages.setChannel(newChannel);
        messages.setMessageReads(newMessageReads);

        assertEquals(newId, messages.getId());
        assertEquals(newContent, messages.getContent());
        assertEquals(newCreatedAt, messages.getCreatedAt());
        assertEquals(newType, messages.getType());
        assertEquals(newRecipient, messages.getRecipient());
        assertEquals(newChannel, messages.getChannel());
        assertEquals(newMessageReads, messages.getMessageReads());
    }
}
