package ru.hse.fcs.tariff.service.domain.measure.unit

import java.math.BigDecimal

class CurrencyUnit(
    val currency: Currency,
    private val exchangeRates: Map<Pair<Currency, Currency>, BigDecimal>
) : LinearConvertible<CurrencyUnit> {

    override fun toString(): String = currency.name

    override fun getConversionRateTo(other: CurrencyUnit): BigDecimal {
        return getConversionRateTo(other.currency)
    }

    private fun getConversionRateTo(other: Currency): BigDecimal {
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

        return rates.minOrNull() ?: throw RuntimeException("bla bla bla")
    }

    fun withCurrency(other: Currency): CurrencyUnit {
        return CurrencyUnit(
            other,
            exchangeRates
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other is CurrencyUnit) {
            return this.currency == other.currency
        }
        return super.equals(other)
    }
}