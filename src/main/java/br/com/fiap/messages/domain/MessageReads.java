package br.com.fiap.messages.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class MessageReads {

    private UUID id;
    private Messages message;
    private String user_id;
    private OffsetDateTime read_at;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Messages getMessage() {
        return message;
    }

    public void setMessage(Messages message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public OffsetDateTime getRead_at() {
        return read_at;
    }

    public void setRead_at(OffsetDateTime read_at) {
        this.read_at = read_at;
    }
}
