package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import java.util.*

open class Product(
    val productId: UUID? = null,
    final override val weight: Weight? = null,
    final override val volume: Volume? = null,
    density: Density? = null,
    override var price: Price? = null,
    override val totalNumber: Long = 1,
) : Parcel {

    override val density: Density? = density ?:
        if (this.weight != null && this.volume != null) {
            Density(this.weight, this.volume)
        } else { null }
}


