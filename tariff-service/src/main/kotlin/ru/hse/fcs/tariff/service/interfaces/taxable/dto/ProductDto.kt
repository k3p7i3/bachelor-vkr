package ru.hse.fcs.tariff.service.interfaces.taxable.dto

import java.util.*

data class ProductDto(
    val productId: UUID? = null,
    val weight: WeightDto? = null,
    val volume: VolumeDto? = null,
    val boxVolume: BoxVolumeDto? = null,
    val density: DensityDto? = null,
    val price: PriceDto? = null,
    val totalNumber: Long = 1L,
    val appliedTariffs: List<AppliedTariffDataDto> = emptyList()
)