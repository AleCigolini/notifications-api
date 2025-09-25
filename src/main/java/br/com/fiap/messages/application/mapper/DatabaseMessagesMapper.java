package br.com.fiap.messages.application.mapper;

import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;
import br.com.fiap.messages.domain.Messages;

public interface DatabaseMessagesMapper {

    JpaMessagesEntity toEntity(Messages messages);

    Messages toDomain(JpaMessagesEntity jpaMessagesEntity);

}
