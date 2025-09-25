package br.com.fiap.messages.common.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message_reads")
public class JpaMessageReadsEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private JpaMessagesEntity message;

    private String user_id;
    private OffsetDateTime read_at;
}