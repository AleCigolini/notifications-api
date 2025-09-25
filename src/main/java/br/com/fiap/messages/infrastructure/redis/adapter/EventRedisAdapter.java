package br.com.fiap.messages.infrastructure.redis.adapter;


import br.com.fiap.messages.common.interfaces.EventRedis;
import br.com.fiap.messages.domain.Event;
import br.com.fiap.messages.infrastructure.sse.adapter.EventSenderAdapter;
import com.google.gson.Gson;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.quarkus.runtime.Startup;
import io.vertx.mutiny.redis.client.Command;
import io.vertx.mutiny.redis.client.Redis;
import io.vertx.mutiny.redis.client.Request;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@RequiredArgsConstructor
@Startup
public class EventRedisAdapter implements EventRedis {

    private static final Logger log = LoggerFactory.getLogger(EventRedisAdapter.class);
    private final Redis redis;
    private final Gson gson;
    private final RedisDataSource redisDS;
    private final EventSenderAdapter eventSenderAdapter;

    private static final String CHANNEL = "notifications-channel";

    @Override
    public void publishEvent(Event event) {
        redis.send(Request.cmd(Command.PUBLISH)
                        .arg(CHANNEL)
                        .arg(gson.toJson(event)))
                .subscribe().with(item -> {
                }, failure -> failure.printStackTrace());
    }

    @Override
    @PostConstruct
    public void subscribe() {
        PubSubCommands<String> pubsub = redisDS.pubsub(String.class);
        pubsub.subscribe(CHANNEL, event -> {
            log.info("Mensagem recebida do Redis: {}", event);
            eventSenderAdapter.send(gson.fromJson(event, Event.class));
        });
    }
}