package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight

interface Parcel {
    val weight: Weight?

    val volume: Volume?

    val density: Density?

    val price: NonConvertiblePrice?

    val totalNumber: Long?
}