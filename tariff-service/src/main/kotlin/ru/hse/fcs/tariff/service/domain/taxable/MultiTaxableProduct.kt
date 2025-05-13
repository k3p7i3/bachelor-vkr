package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import java.util.*

class MultiTaxableProduct(
    productId: UUID? = null,
    weight: Weight?,
    volume: Volume?,
    density: Density?,
    price: Price?,
    totalNumber: Long = 1L,
    override val appliedTariffs: List<AppliedTariffData> = emptyList()
) : Product(
    productId,
    weight,
    volume,
    density,
    price,
    totalNumber
), MultiTaxableParcel