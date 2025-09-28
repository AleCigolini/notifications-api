package br.com.fiap.messages.domain;

import br.com.fiap.messages.common.domain.entity.JpaMessageReadsEntity;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public class Messages {

    private UUID id;
    private String content;
    private OffsetDateTime createdAt;
    private MessageType type;
    private String recipient;
    private StatusType status;
    private Set<JpaMessageReadsEntity> messageReads;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
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

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public Set<JpaMessageReadsEntity> getMessageReads() {
        return messageReads;
    }

    public void setMessageReads(Set<JpaMessageReadsEntity> messageReads) {
        this.messageReads = messageReads;
    }

}
