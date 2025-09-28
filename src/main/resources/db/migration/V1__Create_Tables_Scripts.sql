-- Tabela: messages
-- Armazena todas as mensagens do sistema, gerais (broadcast) e individuais (privadas).
CREATE TABLE messages (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Identificador único da mensagem (UUID).
                          content TEXT NOT NULL,                         -- Conteúdo da mensagem.
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),   -- Data e hora em que a mensagem foi criada.
                          type CHAR(1) NOT NULL,                         -- 'B' (broadcast/geral) ou 'P' (privada/individual).
                          recipient VARCHAR(255),                        -- Identificador do destinatário; NULL para mensagens gerais, username/id para individuais.
                          status VARCHAR(20) NOT NULL                    -- Status da mensagem: INFO, WARNING, ERROR, SUCCESS.
);

COMMENT ON COLUMN messages.id IS 'Identificador único da mensagem (UUID).';
COMMENT ON COLUMN messages.content IS 'Conteúdo da mensagem.';
COMMENT ON COLUMN messages.created_at IS 'Data e hora em que a mensagem foi criada.';
COMMENT ON COLUMN messages.type IS '''B'' para mensagens gerais (broadcast) e ''P'' para mensagens privadas.';
COMMENT ON COLUMN messages.recipient IS 'Identificador do destinatário; NULL para mensagens gerais, username/id para mensagens privadas.';
COMMENT ON COLUMN messages.status IS 'Status da mensagem: INFO, WARNING, ERROR, SUCCESS.';

-- Tabela: message_reads
-- Controla quais usuários leram cada mensagem e quando.
CREATE TABLE message_reads (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Identificador único do registro de leitura (UUID).
                               message_id UUID NOT NULL REFERENCES messages(id) ON DELETE CASCADE, -- Referência à mensagem lida.
                               user_id VARCHAR(255) NOT NULL,                 -- Identificador do usuário que leu a mensagem.
                               read_at TIMESTAMP NOT NULL DEFAULT NOW(),      -- Data e hora em que a mensagem foi lida.
                               UNIQUE(message_id, user_id)                    -- Garante que cada usuário só tenha um registro de leitura por mensagem.
);

COMMENT ON COLUMN message_reads.id IS 'Identificador único do registro de leitura (UUID).';
COMMENT ON COLUMN message_reads.message_id IS 'Referência à mensagem lida (UUID).';
COMMENT ON COLUMN message_reads.user_id IS 'Identificador do usuário que leu a mensagem.';
COMMENT ON COLUMN message_reads.read_at IS 'Data e hora em que a mensagem foi lida.';
