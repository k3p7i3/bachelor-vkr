package ru.hse.fcs.tariff.service.application

import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyExchangeRate
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyRepository
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyUnit
import ru.hse.fcs.tariff.service.domain.tariff.Tariff
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition
import java.math.BigDecimal
import java.util.*

@Component
class CurrencyService(
    private val currencyRepository: CurrencyRepository
) {
    fun saveCurrencyRate(
        agentId: UUID,
        currencyExchangeRate: CurrencyExchangeRate
    ) {
        currencyRepository.saveCurrencyConversionRate(agentId, currencyExchangeRate)
    }

    fun saveCurrencyRates(
        agentId: UUID,
        currencyExchangeRates: List<CurrencyExchangeRate>
    ): List<CurrencyExchangeRate> {
        return currencyRepository.saveCurrencyConversionRates(agentId, currencyExchangeRates)
    }

    fun getCurrencyExchangeRates(
        agentId: UUID
    ) = currencyRepository.getCurrencyConversionRates(agentId)

    fun getCurrencyExchangeRatesForTariff(
        agentId: UUID,
    ): Map<Pair<Currency, Currency>, BigDecimal> {
        val exchangeRatesMap = mutableMapOf<Pair<Currency, Currency>, BigDecimal>()
        Currency.entries.forEach {
            exchangeRatesMap[it to it] = BigDecimal.ONE
        }

        val rates: List<CurrencyExchangeRate> = currencyRepository.getCurrencyConversionRates(agentId)
        rates.forEach {
            exchangeRatesMap[it.from to it.to] = it.rate
        }
        return exchangeRatesMap
    }
}