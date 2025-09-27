package br.com.fiap.messages.infrastructure.database.adapter;

import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;
import br.com.fiap.messages.infrastructure.database.repository.JpaMessagesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagesJpaAdapterTest {

    @Mock
    private JpaMessagesRepository jpaMessagesRepository;

    @InjectMocks
    private MessagesJpaAdapter messagesJpaAdapter;

    private JpaMessagesEntity messagesEntity;
    private final String testRecipient = "test-user";

    @BeforeEach
    void setUp() {
        messagesEntity = new JpaMessagesEntity();
        messagesEntity.setId(UUID.randomUUID());
        messagesEntity.setRecipient(testRecipient);
        messagesEntity.setContent("Test message content");
        messagesEntity.setCreatedAt(OffsetDateTime.now());
    }

    @Test
    void shouldSaveMessagesEntitySuccessfully() {
        doNothing().when(jpaMessagesRepository).persist(any(JpaMessagesEntity.class));

        JpaMessagesEntity result = messagesJpaAdapter.save(messagesEntity);

        verify(jpaMessagesRepository, times(1)).persist(messagesEntity);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(messagesEntity);
    }

    @Test
    void shouldGetMessagesByRecipientSuccessfully() {
        List<JpaMessagesEntity> expectedMessages = Arrays.asList(messagesEntity);
        when(jpaMessagesRepository.list(eq("recipient"), eq(testRecipient))).thenReturn(expectedMessages);

        List<JpaMessagesEntity> result = messagesJpaAdapter.getMessagesByRecipient(testRecipient);

        verify(jpaMessagesRepository, times(1)).list("recipient", testRecipient);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRecipient()).isEqualTo(testRecipient);
    }

    @Test
    void shouldReturnEmptyListWhenNoMessagesForRecipient() {
        String nonExistentRecipient = "non-existent-user";
        when(jpaMessagesRepository.list(eq("recipient"), eq(nonExistentRecipient))).thenReturn(List.of());

        List<JpaMessagesEntity> result = messagesJpaAdapter.getMessagesByRecipient(nonExistentRecipient);

        verify(jpaMessagesRepository, times(1)).list("recipient", nonExistentRecipient);
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
}
