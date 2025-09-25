package br.com.fiap.messages.application.mapper.impl;

import br.com.fiap.messages.application.mapper.DatabaseMessagesMapper;
import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;
import br.com.fiap.messages.domain.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@ApplicationScoped
@AllArgsConstructor
public class DatabaseMessagesMapperImpl implements DatabaseMessagesMapper {

    private ModelMapper modelMapper;

    @Override
    public JpaMessagesEntity toEntity(Messages messages) {
        return modelMapper.map(messages, JpaMessagesEntity.class);
    }

    @Override
    public Messages toDomain(JpaMessagesEntity jpaMessagesEntity) {
        return modelMapper.map(jpaMessagesEntity, Messages.class);
    }
}
