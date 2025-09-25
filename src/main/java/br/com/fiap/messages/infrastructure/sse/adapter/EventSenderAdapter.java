package br.com.fiap.messages.infrastructure.sse.adapter;

import br.com.fiap.messages.common.interfaces.EventSender;
import br.com.fiap.messages.domain.Event;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.MultiEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
@AllArgsConstructor
public class EventSenderAdapter implements EventSender {
    private static final Logger log = LoggerFactory.getLogger(EventSenderAdapter.class);
    private final Map<String, List<MultiEmitter<? super Event>>> clients = new ConcurrentHashMap<>();

    public Multi<Event> subscribe(String clientId) {
        return Multi.createFrom().emitter(emitter -> {
            log.info("Cliente conectado: {}", clientId);
            clients.computeIfAbsent(clientId, k -> new CopyOnWriteArrayList<>()).add(emitter);

            emitter.onTermination(() -> {
                log.info("Cliente desconectado: {}", clientId);
                List<MultiEmitter<? super Event>> emitters = clients.get(clientId);
                if (emitters != null) {
                    emitters.remove(emitter);
                    if (emitters.isEmpty()) {
                        clients.remove(clientId);
                    }
                }
            });
        });
    }

    public void send(Event event) {
        if (event == null || event.getEventPayload() == null) return;

        if ("B".equals(event.getType().name())) {
            // Broadcast para todos os emitters de todos os clientes
            clients.values().forEach(emitterList -> emitterList.forEach(emitter -> emitter.emit(event)));
        } else if (event.getRecipient() != null) {
            // Envia para todos os emitters do clientId específico
            List<MultiEmitter<? super Event>> emitters = clients.get(event.getRecipient());
            if (emitters != null) {
                emitters.forEach(emitter -> emitter.emit(event));
            }
        }
    }
}