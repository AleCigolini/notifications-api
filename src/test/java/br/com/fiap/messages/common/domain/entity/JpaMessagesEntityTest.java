package br.com.fiap.messages.common.domain.entity;

import br.com.fiap.messages.domain.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JpaMessagesEntityTest {

    private JpaMessagesEntity messagesEntity;
    private final UUID testId = UUID.randomUUID();
    private final String testContent = "Test Message Content";
    private final OffsetDateTime testCreatedAt = OffsetDateTime.now();
    private final MessageType testType = MessageType.B;
    private final String testRecipient = "testUser@example.com";
    private final String testChannel = "EMAIL";

    @BeforeEach
    void setUp() {
        messagesEntity = new JpaMessagesEntity();
    }

    @Nested
    class BasicPropertiesTests {

        @Test
        void shouldSetAndGetIdCorrectly() {
            messagesEntity.setId(testId);
            assertEquals(testId, messagesEntity.getId());
        }

        @Test
        void shouldSetAndGetContentCorrectly() {
            messagesEntity.setContent(testContent);
            assertEquals(testContent, messagesEntity.getContent());
        }

        @Test
        void shouldSetAndGetCreatedAtCorrectly() {
            messagesEntity.setCreatedAt(testCreatedAt);
            assertEquals(testCreatedAt, messagesEntity.getCreatedAt());
        }

        @ParameterizedTest
        @EnumSource(MessageType.class)
        void shouldSetAndGetMessageTypeCorrectly(MessageType type) {
            messagesEntity.setType(type);
            assertEquals(type, messagesEntity.getType());
        }

        @Test
        void shouldSetAndGetRecipientCorrectly() {
            messagesEntity.setRecipient(testRecipient);
            assertEquals(testRecipient, messagesEntity.getRecipient());
        }

        @Test
        void shouldSetAndGetChannelCorrectly() {
            messagesEntity.setChannel(testChannel);
            assertEquals(testChannel, messagesEntity.getChannel());
        }
    }

    @Nested
    class LifecycleCallbackTests {

        @Test
        void shouldSetCreatedAtDuringPrePersistIfNull() {
            messagesEntity.setCreatedAt(null);
            assertNull(messagesEntity.getCreatedAt());

            messagesEntity.prePersist();

            assertNotNull(messagesEntity.getCreatedAt());
        }

        @Test
        void shouldNotChangeCreatedAtDuringPrePersistIfAlreadySet() {
            messagesEntity.setCreatedAt(testCreatedAt);
            assertEquals(testCreatedAt, messagesEntity.getCreatedAt());

            messagesEntity.prePersist();

            assertEquals(testCreatedAt, messagesEntity.getCreatedAt());
        }
    }

    @Nested
    class ConstructorTests {

        @Test
        void shouldCreateEntityUsingNoArgsConstructor() {
            JpaMessagesEntity entity = new JpaMessagesEntity();
            assertNotNull(entity);
        }

        @Test
        void shouldCreateEntityUsingAllArgsConstructor() {
            Set<JpaMessageReadsEntity> messageReads = new HashSet<>();

            JpaMessagesEntity entity = new JpaMessagesEntity(
                    testId,
                    testContent,
                    testCreatedAt,
                    testType,
                    testRecipient,
                    testChannel,
                    messageReads
            );

            assertEquals(testId, entity.getId());
            assertEquals(testContent, entity.getContent());
            assertEquals(testCreatedAt, entity.getCreatedAt());
            assertEquals(testType, entity.getType());
            assertEquals(testRecipient, entity.getRecipient());
            assertEquals(testChannel, entity.getChannel());
            assertEquals(messageReads, entity.getMessageReads());
        }
    }

    @Nested
    class EqualsAndHashCodeTests {

        @Test
        void shouldBeEqualBasedOnSameId() {
            JpaMessagesEntity entity1 = new JpaMessagesEntity();
            entity1.setId(testId);

            JpaMessagesEntity entity2 = new JpaMessagesEntity();
            entity2.setId(testId);

            assertEquals(entity1, entity2);
            assertEquals(entity1.hashCode(), entity2.hashCode());
        }

        @Test
        void shouldNotBeEqualBasedOnDifferentId() {
            JpaMessagesEntity entity1 = new JpaMessagesEntity();
            entity1.setId(testId);

            JpaMessagesEntity entity2 = new JpaMessagesEntity();
            entity2.setId(UUID.randomUUID());

            assertNotEquals(entity1, entity2);
            assertNotEquals(entity1.hashCode(), entity2.hashCode());
        }
    }
}
