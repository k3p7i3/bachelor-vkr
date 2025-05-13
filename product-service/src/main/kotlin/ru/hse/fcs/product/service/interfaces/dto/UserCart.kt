package ru.hse.fcs.product.service.interfaces.dto

import java.time.LocalDateTime
import java.util.UUID

data class UserCart(
    val userId: UUID,
    val products: List<CartProduct>
) {
    data class CartProduct(
        val id: UUID,
        val userId: UUID? = null,
        val product: ProductDto? = null,
        val count: Long,
        val createdAt: LocalDateTime
    )
}