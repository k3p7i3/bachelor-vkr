package ru.hse.fcs.product.service.interfaces.dto

import java.util.UUID

data class AddProductToFavoritesRequest(
    var userId: UUID,
    var productId: UUID? = null,
    var product: ProductDto? = null
)