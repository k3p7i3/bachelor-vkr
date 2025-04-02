package ru.hse.fcs.tariff.service.domain.measure.unit

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID

@Component
class CurrencyUnitFactory(
    private val currencyRepository: CurrencyRepository
) {

    fun buildCurrencyUnit(agentId: UUID, currency: Currency) : CurrencyUnit {
        val exchangeRatesMap = mutableMapOf<Pair<Currency, Currency>, BigDecimal>()
        Currency.entries.forEach {
            exchangeRatesMap[it to it] = BigDecimal.ONE
        }

        val rates: List<CurrencyExchangeRate> = currencyRepository.getCurrencyConversionRates(agentId)
        rates.forEach {
            exchangeRatesMap[it.from to it.to] = it.rate
        }

        return CurrencyUnit(currency, exchangeRatesMap)
    }
}