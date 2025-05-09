package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import java.math.BigDecimal
import java.util.*

open class Order(
    val orderId: UUID? = null,
    val products: List<Product>,
    weight: Weight? = null,
    volume: Volume? = null,
    density: Density? = null,
    price: Price? = null,
    totalNumber: Long? = null
) : Parcel {

    final override val weight: Weight? = weight ?:
        products
            .mapNotNull {
                it.weight?.let { w ->
                    Weight(
                        w.value.multiply(it.totalNumber.toBigDecimal()),
                        unit = w.unit
                    )
                }
            }
            .reduceOrNull(Weight::plus)

    final override val volume: Volume? = volume ?:
        products
            .mapNotNull {
                it.volume?.let { v ->
                    Volume(
                        v.value.multiply(it.totalNumber.toBigDecimal()),
                        unit = v.unit
                    )
                }
            }
            .reduceOrNull(Volume::plus)

    override var density: Density? = density ?:
        if (this.weight != null && this.volume != null) {
            Density(this.weight, this.volume)
        } else { null }

    override var price: Price? = price

    override val totalNumber: Long? = totalNumber ?:
        products
            .map { it.totalNumber ?: 1 }
            .reduceOrNull(Long::plus)

    override fun setCurrencyExchangeRates(rates: Map<Pair<Currency, Currency>, BigDecimal>) {
        products.forEach { it.setCurrencyExchangeRates(rates) }

        price?.unit?.exchangeRates = rates
        if (price == null) {
            price = products
                .mapNotNull {
                    it.price?.let { p ->
                        Price(
                            p.value.multiply(it.totalNumber.toBigDecimal()),
                            p.unit
                        )
                    }
                }
                .reduceOrNull(Price::plus)
        }
    }
}

