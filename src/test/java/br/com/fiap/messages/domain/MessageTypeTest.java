package br.com.fiap.messages.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageTypeTest {

    @Test
    void shouldHaveCorrectEnumValues() {
        assertEquals(2, MessageType.values().length);
        assertEquals("B", MessageType.B.name());
        assertEquals("P", MessageType.P.name());
    }

    @Test
    void shouldConvertFromStringToEnum() {
        assertEquals(MessageType.B, MessageType.valueOf("B"));
        assertEquals(MessageType.P, MessageType.valueOf("P"));
    }

    @Test
    void shouldThrowExceptionForInvalidEnumValue() {
        assertThrows(IllegalArgumentException.class, () -> MessageType.valueOf("INVALID"));
    }

    @Test
    void shouldGetCorrectOrdinalValues() {
        assertEquals(0, MessageType.B.ordinal());
        assertEquals(1, MessageType.P.ordinal());
    }
}