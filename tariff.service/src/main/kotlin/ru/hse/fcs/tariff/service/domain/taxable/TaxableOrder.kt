package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import java.util.UUID

class TaxableOrder(
    orderId: UUID,
    products: List<Product>,
    weight: Weight? = null,
    volume: Volume? = null,
    density: Density? = null,
    price: NonConvertiblePrice? = null,
    totalNumber: Long? = null,
    override val appliedTariff: AppliedTariffData
) : Order(
    orderId,
    products,
    weight,
    volume,
    density,
    price,
    totalNumber,
), TaxableParcel