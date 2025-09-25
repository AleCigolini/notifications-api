package br.com.fiap.messages.common.interfaces;

import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;

import java.util.List;

public interface MessagesDatabase {

    JpaMessagesEntity save(JpaMessagesEntity messagesEntity);
    List<JpaMessagesEntity> getMessagesByRecipient(String recipient);
}
