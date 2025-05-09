package ru.hse.fcs.product.service.interfaces.dto

import java.util.UUID

data class AddProductToCartRequest(
    var userId: UUID,
    var productId: UUID? = null,
    var product: ProductDto? = null,
    var count: Long = 1L
)