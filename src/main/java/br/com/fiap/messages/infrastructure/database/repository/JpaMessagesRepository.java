package br.com.fiap.messages.infrastructure.database.repository;

import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class JpaMessagesRepository implements PanacheRepositoryBase<JpaMessagesEntity, UUID> {

}
