package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import java.util.*

open class Product(
    val productId: UUID,
    override val weight: Weight? = null,
    override val volume: Volume? = null,
    override val density: Density? = null,
    override val price: NonConvertiblePrice? = null,
    override val totalNumber: Long? = null,
) : Parcel


