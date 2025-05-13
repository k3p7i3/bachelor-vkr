package ru.hse.fcs.order.service.domain.model

import ru.hse.fcs.order.service.domain.model.measurement.*
import java.util.*

data class OrderProduct(
    val productId: UUID,
    var totalNumber: Long,
    var price: Price? = null,
    var weight: Weight? = null,
    var volume: Volume? = null,
    var boxVolume: BoxVolume? = null,
    var density: Density? = null
)