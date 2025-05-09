package ru.hse.fcs.product.service.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("favorite_product")
data class FavoriteProduct(
    @Id
    @Column("id")
    val id: UUID? = null,

    @Column("user_id")
    val userId: UUID,

    @Column("product_id")
    val productId: UUID,

    @CreatedDate
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
)