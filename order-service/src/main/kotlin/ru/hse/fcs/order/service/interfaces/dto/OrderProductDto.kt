package ru.hse.fcs.order.service.interfaces.dto

import ru.hse.fcs.order.service.interfaces.dto.measurements.*
import java.util.*

data class OrderProductDto(
    var productId: UUID,
    var weight: WeightDto? = null,
    var volume: VolumeDto? = null,
    var boxVolume: BoxVolumeDto? = null,
    var density: DensityDto? = null,
    var price: PriceDto? = null,
    var totalNumber: Long
)