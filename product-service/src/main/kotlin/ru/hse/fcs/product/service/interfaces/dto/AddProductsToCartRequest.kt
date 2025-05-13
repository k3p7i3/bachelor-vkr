package ru.hse.fcs.product.service.interfaces.dto

import java.util.UUID

data class AddProductsToCartRequest(
    var userId: UUID,
    var products: List<ProductToCount>,
) {
    data class ProductToCount(
        var product: ProductDto,
        var count: Long
    )
}