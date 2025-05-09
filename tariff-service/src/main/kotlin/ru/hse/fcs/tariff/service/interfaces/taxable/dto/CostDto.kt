package ru.hse.fcs.tariff.service.interfaces.taxable.dto

import ru.hse.fcs.tariff.service.interfaces.tariff.dto.TariffPriceDto
import java.math.BigDecimal

data class CostDto(
    val pricePerUnit: TariffPriceDto,
    val unitAmount: BigDecimal,
    val cost: PriceDto,
    val resultCost: PriceDto
)