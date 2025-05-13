package ru.hse.fcs.order.service.interfaces.dto

import ru.hse.fcs.order.service.interfaces.dto.measurements.*
import java.util.*

data class OrderDto(
    var id: UUID? = null,
    val agentId: UUID,
    val clientId: UUID,
    var products: List<OrderProductDto> = emptyList(),
    var appliedTariffs: List<OrderTariffDto> = emptyList(),
    var payments: List<OrderPaymentDto> = emptyList(),
    var weight: MeasurementDto<WeightDto>? = null,
    var volume: MeasurementDto<VolumeDto>? = null,
    var boxVolume: MeasurementDto<BoxVolumeDto>? = null,
    var density: MeasurementDto<DensityDto>? = null,
    var price: MeasurementDto<PriceDto>? = null,
    var totalNumber: Long? = null,
    var totalCost: PriceDto? = null,
    var paidAmount: PriceDto? = null
) {
    data class MeasurementDto<T>(
        val original: T,
        val normalized: T? = null
    )
}

