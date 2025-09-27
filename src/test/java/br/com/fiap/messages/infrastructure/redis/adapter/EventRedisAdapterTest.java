package br.com.fiap.messages.infrastructure.redis.adapter;

import br.com.fiap.messages.domain.Event;
import br.com.fiap.messages.domain.EventPayload;
import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.infrastructure.sse.adapter.EventSenderAdapter;
import com.google.gson.Gson;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.UniSubscribe;
import io.vertx.mutiny.redis.client.Redis;
import io.vertx.mutiny.redis.client.Request;
import io.vertx.mutiny.redis.client.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRedisAdapterTest {

    private static final String CHANNEL = "notifications-channel";

    @Mock
    private Redis redis;

    @Mock
    private Gson gson;

    @Mock
    private RedisDataSource redisDS;

    @Mock
    private EventSenderAdapter eventSenderAdapter;

    @Mock
    private Uni<Response> responseUni;

    @Mock
    private Request request;

    @Mock
    private PubSubCommands<String> pubSubCommands;

    @Captor
    private ArgumentCaptor<Request> requestCaptor;

    @Captor
    private ArgumentCaptor<Consumer<String>> consumerCaptor;

    @InjectMocks
    private EventRedisAdapter eventRedisAdapter;

    private Event testEvent;
    private String testEventJson;

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setId(UUID.randomUUID().toString());
        testEvent.setType(MessageType.P);
        testEvent.setRecipient("test-user");

        EventPayload payload = new EventPayload();
        payload.setContent("Test message content");
        payload.setCreatedAt(OffsetDateTime.now());
        testEvent.setEventPayload(payload);

        testEventJson = "{\"id\":\"" + testEvent.getId() + "\",\"type\":\"P\",\"recipient\":\"test-user\",\"eventPayload\":{\"content\":\"Test message content\"}}";

        lenient().when(gson.toJson(any(Event.class))).thenReturn(testEventJson);
    }

    @Test
    void shouldPublishEventSuccessfully() {
        when(redis.send(any(Request.class))).thenReturn(responseUni);

        UniSubscribe<Response> mockUniSubscribe = mock(UniSubscribe.class);
        when(responseUni.subscribe()).thenReturn(mockUniSubscribe);

        eventRedisAdapter.publishEvent(testEvent);

        verify(gson).toJson(testEvent);
        verify(redis).send(requestCaptor.capture());

        Request capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest).isNotNull();

        verify(responseUni).subscribe();
        verify(mockUniSubscribe).with(
            any(Consumer.class),
            any(Consumer.class)
        );
    }

    @Test
    void shouldSubscribeAndProcessIncomingEvents() {
        when(redisDS.pubsub(String.class)).thenReturn(pubSubCommands);
        when(gson.fromJson(eq(testEventJson), eq(Event.class))).thenReturn(testEvent);

        eventRedisAdapter.subscribe();

        verify(redisDS).pubsub(String.class);
        verify(pubSubCommands).subscribe(eq(CHANNEL), consumerCaptor.capture());

        Consumer<String> capturedConsumer = consumerCaptor.getValue();
        capturedConsumer.accept(testEventJson);

        verify(gson).fromJson(testEventJson, Event.class);
        verify(eventSenderAdapter).send(testEvent);
    }
}
