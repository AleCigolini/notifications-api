package br.com.fiap.messages.common.domain.entity;

import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class JpaMessagesEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String content;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 1, nullable = false)
    private MessageType type;

    private String recipient;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatusType status;

    @OneToMany(mappedBy = "message")
    private Set<JpaMessageReadsEntity> messageReads;

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
    }
}