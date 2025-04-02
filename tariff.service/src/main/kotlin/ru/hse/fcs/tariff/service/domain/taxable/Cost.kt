package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit
import java.math.BigDecimal

class Cost(
    val pricePerUnit: PricePerUnit,
    val unitAmount: BigDecimal,
    val cost: Price
) {
}