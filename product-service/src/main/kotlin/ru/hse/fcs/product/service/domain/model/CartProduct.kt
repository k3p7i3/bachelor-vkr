package ru.hse.fcs.product.service.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("cart_product")
data class CartProduct(
    @Id
    @Column("id")
    val id: UUID? = null,

    @Column("product_id")
    val productId: UUID,

    @Column("user_id")
    val userId: UUID,

    @CreatedDate
    @Column("created_at")
    val createdAt: LocalDateTime? = null,

    @Column("count")
    var count: Long
)