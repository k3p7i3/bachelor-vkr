package ru.hse.fcs.tariff.service.interfaces.taxable.dto

import java.util.*

data class OrderDto(
    val orderId: UUID? = null,
    val products: List<ProductDto>,
    val weight: MeasurementDto<WeightDto>? = null,
    val volume: MeasurementDto<VolumeDto>? = null,
    val boxVolume: MeasurementDto<BoxVolumeDto>? = null,
    val density: MeasurementDto<DensityDto>? = null,
    val price: MeasurementDto<PriceDto>? = null,
    val totalNumber: Long? = null,
    val appliedTariffs: List<AppliedTariffDataDto> = emptyList()
) {
    data class MeasurementDto<T>(
        val original: T,
        val normalized: T? = null
    )
}
