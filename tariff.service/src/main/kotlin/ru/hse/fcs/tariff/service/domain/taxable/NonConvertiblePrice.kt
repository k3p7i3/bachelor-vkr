package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyUnit
import java.math.BigDecimal

class NonConvertiblePrice(
    val value: BigDecimal,
    val currency: Currency
) {

    operator fun plus(other: NonConvertiblePrice): NonConvertiblePrice {
        if (currency != other.currency) {
            throw IllegalArgumentException("Product with different currency in one order")
        }
        return NonConvertiblePrice(
            value + other.value,
            currency
        )
    }

    fun toPrice(currencyUnit: CurrencyUnit): Price =
        Price(
            value,
            currencyUnit.withCurrency(currency)
        )
}