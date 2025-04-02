package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import java.util.*

open class Order(
    val orderId: UUID,
    val products: List<Product>,
    weight: Weight? = null,
    volume: Volume? = null,
    density: Density? = null,
    price: NonConvertiblePrice? = null,
    totalNumber: Long? = null
) : Parcel {

    override val weight: Weight? = weight ?:
        products
            .mapNotNull { it.weight }
            .reduceOrNull(Weight::plus)

    override val volume: Volume? = volume ?:
        products
            .mapNotNull { it.volume }
            .reduceOrNull(Volume::plus)

    override val density: Density? = density ?:
        if (weight != null && volume != null) {
            Density(weight, volume)
        } else { null }

    override val price: NonConvertiblePrice? = price ?:
        products
            .mapNotNull { it.price }
            .reduceOrNull(NonConvertiblePrice::plus)

    override val totalNumber: Long? = totalNumber ?:
        products
            .map { it.totalNumber ?: 1 }
            .reduceOrNull(Long::plus)
}

