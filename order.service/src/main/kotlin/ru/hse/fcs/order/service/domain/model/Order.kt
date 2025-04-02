package ru.hse.fcs.order.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import ru.hse.fcs.order.service.domain.model.measurement.*
import java.util.*

@Table("order")
data class Order(
    @Id
    @Column("id")
    val id: UUID,

    @Column("agent_id")
    val agentId: UUID,

    @Column("client_id")
    val clientId: UUID,

    // user can manipulate it when creating order, agent can fix it anytime?
    @Column("weight")
    var weight: Weight? = null,

    @Column("volume")
    var volume: Volume? = null,

    @Column("box_volume")
    var boxVolume: BoxVolume? = null,

    @Column("density")
    var density: Density? = null,

    @Column("product_total_price")
    var productTotalPrice: Price? = null
)