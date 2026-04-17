package com.e.commerce.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.e.commerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidade que representa um pedido no sistema de e-commerce.
 *
 * <p>Um pedido contém:
 * <ul>
 *   <li>Um usuário que realizou o pedido</li>
 *   <li>Múltiplos itens do pedido (OrderItem)</li>
 *   <li>Um pagamento opcional (Payment)</li>
 *   <li>Um status que transita entre: AGUARDANDO_PAGAMENTO → PAGO → ENVIADO → ENTREGUE</li>
 * </ul>
 *
 * <p>Status do Pedido:
 * <ul>
 *   <li>AGUARDANDO_PAGAMENTO: Pedido criado, aguardando pagamento</li>
 *   <li>PAGO: Pagamento confirmado, pronto para envio</li>
 *   <li>ENVIADO: Pedido despachado para o cliente</li>
 *   <li>ENTREGUE: Pedido entregue ao cliente</li>
 * </ul>
 *
 * <p>Relacionamentos:
 * <ul>
 *   <li>Muitos-para-Um com User (cada pedido pertence a um usuário)</li>
 *   <li>Um-para-Muitos com OrderItem (cada pedido tem múltiplos itens)</li>
 *   <li>Um-para-Um com Payment (cada pedido pode ter um pagamento)</li>
 * </ul>
 *
 * @author E-Commerce Team
 * @version 1.0
 * @since 2026-04-17
 */
@Entity
@Table(name = "tb_orders", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    /**
     * Identificador único do pedido (UUID).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Data e hora em que o pedido foi criado.
     * Definida automaticamente pelo banco.
     */
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime moment;

    /**
     * Status atual do pedido.
     * Valores válidos: AGUARDANDO_PAGAMENTO, PAGO, ENVIADO, ENTREGUE
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * Usuário que realizou o pedido.
     * Relacionamento muitos-para-um.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    /**
     * Pagamento associado ao pedido (opcional).
     * Pode ser nulo enquanto pedido está em AGUARDANDO_PAGAMENTO.
     */
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Payment payment;

    /**
     * Itens inclusos no pedido.
     * Cada item contém produto, quantidade e preço no momento do pedido.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * Timestamp de quando o pedido foi criado.
     * Definido automaticamente pelo banco.
     */
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Timestamp de última atualização.
     * Atualizado automaticamente a cada modificação.
     */
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
