package ru.hse.fcs.order.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import ru.hse.fcs.order.service.domain.model.measurement.*
import java.util.*

@Table("order_product")
class OrderProduct(
    @Id
    @Column("id")
    val id: UUID? = null,

    @Column("order_id")
    val orderId: UUID,

    @Column("product_id")
    val productId: UUID,

    @Column("number")
    val number: Long,

    // user can manipulate it when creating order, agent can fix it anytime?
    @Column("price")
    var price: Price,

    @Column("weight")
    var weight: Weight,

    @Column("volume")
    var volume: Volume,

    @Column("boxVolume")
    var boxVolume: BoxVolume,

    @Column("density")
    var density: Density
)