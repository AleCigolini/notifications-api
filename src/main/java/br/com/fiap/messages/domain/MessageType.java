package br.com.fiap.messages.domain;

/**
 * Enum que representa os tipos de mensagem possíveis.
 * <ul>
 *   <li>B - Broadcast: mensagem geral enviada para todos os destinatários.</li>
 *   <li>P - Privada: mensagem enviada para um destinatário específico.</li>
 * </ul>
 */
public enum MessageType {
    /**
     * Broadcast - mensagem geral para todos.
     */
    B,

    /**
     * Privada - mensagem enviada a um destinatário específico.
     */
    P
}