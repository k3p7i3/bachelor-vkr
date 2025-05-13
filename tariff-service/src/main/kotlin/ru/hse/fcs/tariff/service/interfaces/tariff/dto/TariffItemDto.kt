package ru.hse.fcs.tariff.service.interfaces.tariff.dto

import java.util.*

data class TariffItemDto(
    var id: UUID? = null,
    var price: TariffPriceDto
)