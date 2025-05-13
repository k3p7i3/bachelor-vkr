package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import java.math.BigDecimal

interface Parcel {
    val weight: Weight?

    val volume: Volume?

    val density: Density?

    var price: Price?

    val totalNumber: Long?

    fun setCurrencyExchangeRates(
        rates: Map<Pair<Currency, Currency>, BigDecimal>
    ) {
        price?.unit?.exchangeRates = rates
    }
}