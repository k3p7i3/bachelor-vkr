package ru.hse.fcs.tariff.service.domain.measure.unit

import ru.hse.fcs.tariff.service.domain.taxable.exception.CurrencyRateNotFound
import java.math.BigDecimal

class CurrencyUnit(
    val currency: Currency,
    var exchangeRates: Map<Pair<Currency, Currency>, BigDecimal> = emptyMap()
) : LinearConvertible<CurrencyUnit> {

    override fun toString(): String = currency.name

    override fun getConversionRateTo(other: CurrencyUnit): BigDecimal {
        return getConversionRateTo(other.currency)
    }

    private fun getConversionRateTo(other: Currency): BigDecimal {
        if (currency == other) {
            return BigDecimal.ONE
        }
        val rates = Currency.entries
            .mapNotNull { interimCurr ->
                val exchangeRateFrom = exchangeRates[currency to interimCurr]
                val exchangeRateTo = exchangeRates[interimCurr to other]

                if (exchangeRateFrom != null && exchangeRateTo != null) {
                    exchangeRateFrom * exchangeRateTo
                } else {
                    null
                }
            }

        return rates.minOrNull() ?:
            throw CurrencyRateNotFound("No currency rate from $currency to $other")
    }


    override fun equals(other: Any?): Boolean {
        if (other is CurrencyUnit) {
            return this.currency == other.currency
        }
        return super.equals(other)
    }
}