package br.com.fiap.messages.infrastructure.database.adapter;

import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;
import br.com.fiap.messages.common.interfaces.MessagesDatabase;
import br.com.fiap.messages.infrastructure.database.repository.JpaMessagesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
@Transactional
public class MessagesJpaAdapter implements MessagesDatabase {

    private JpaMessagesRepository jpaMessagesRepository;

    @Override
    public JpaMessagesEntity save(JpaMessagesEntity messagesEntity) {
        jpaMessagesRepository.persist(messagesEntity);
        return messagesEntity;
    }

    @Override
    public List<JpaMessagesEntity> getMessagesByRecipient(String recipient) {
        return jpaMessagesRepository.list("recipient", recipient);
    }
}
