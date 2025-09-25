package br.com.fiap.messages.domain;

public class Event {

    private String id;
    private MessageType type;
    private String recipient;
    private EventPayload eventPayload;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public EventPayload getEventPayload() {
        return eventPayload;
    }

    public void setEventPayload(EventPayload eventPayload) {
        this.eventPayload = eventPayload;
    }
}