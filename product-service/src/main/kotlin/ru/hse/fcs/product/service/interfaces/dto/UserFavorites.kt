package ru.hse.fcs.product.service.interfaces.dto

import java.time.LocalDateTime
import java.util.*

data class UserFavorites(
    val userId: UUID,
    val products: List<FavoriteProduct>
) {

    data class FavoriteProduct(
        val id: UUID,
        val userId: UUID? = null,
        val product: ProductDto,
        val createdAt: LocalDateTime
    )
}